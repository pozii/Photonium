package org.pozi.photonium.mixin.client.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.util.math.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.pozi.photonium.util.PhotoniumLogger;

import java.lang.reflect.Field;
import java.util.List;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbMergeMixin {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("Photonium");

    @Unique
    private static Field AMOUNT_FIELD;

    // Yansıma ile "amount" alanını bulma işlemi (Static blok - Sadece bir kez çalışır)
    static {
        try {
            for (Field f : ExperienceOrbEntity.class.getDeclaredFields()) {
                // İsimden bağımsız olarak, ExperienceOrbEntity içindeki 'int' tipindeki alanı arıyoruz.
                // Genelde 'amount', 'value' veya 'd' gibi isimleri olur.
                if (f.getType() == int.class) {
                    f.setAccessible(true);
                    AMOUNT_FIELD = f;
                    // İlk bulduğumuz int alanı muhtemelen amount'tur (En önemli veri odur).
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Photonium: XP Amount field not found via reflection!", e);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        // Kendimizi Object üzerinden alıyoruz
        ExperienceOrbEntity self = (ExperienceOrbEntity) (Object) this;

        // "age" alanı genellikle public erişime sahiptir veya methodla alınabilir.
        // Ama riske girmemek için Entity'nin yaşını getter ile değil direkt tick sayacından tahmin edebiliriz
        // veya basitçe her zaman çalıştırıp olasılıkla sınırlarız.
        // En garantisi: MinecraftClient üzerinden dünyayı kontrol etmektir.

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;

        // Performans: Sadece %5 şansla çalış (Ortalama 20 tickte bir denk gelir)
        // age değişkenine erişim hatası almamak için Math.random kullanıyoruz.
        if (Math.random() > 0.05) return;

        mergeNearbyOrbs(self, client);
    }

    @Unique
    private void mergeNearbyOrbs(ExperienceOrbEntity self, MinecraftClient client) {
        if (AMOUNT_FIELD == null) return;

        // Global Client World kullanarak etraftaki varlıkları tarıyoruz.
        // HATA ÇÖZÜMÜ: self.getWorld() yerine client.world kullanıldı.
        Box searchBox = self.getBoundingBox().expand(0.5D);

        List<ExperienceOrbEntity> nearbyOrbs = client.world.getEntitiesByClass(
                ExperienceOrbEntity.class,
                searchBox,
                entity -> entity != self && !entity.isRemoved() // HATA ÇÖZÜMÜ: isAlive yerine !isRemoved
        );

        for (ExperienceOrbEntity otherOrb : nearbyOrbs) {
            try {
                // Yansıma ile değerleri oku
                int myAmount = AMOUNT_FIELD.getInt(self);
                int otherAmount = AMOUNT_FIELD.getInt(otherOrb);

                // Değerleri topla
                int newAmount = myAmount + otherAmount;

                // Yeni değeri kendime yaz
                AMOUNT_FIELD.setInt(self, newAmount);

                // Diğer topu sil
                // HATA ÇÖZÜMÜ: discard() yerine remove() kullanıldı.
                otherOrb.remove(Entity.RemovalReason.DISCARDED);
                PhotoniumLogger.logXpMerge();

                // Döngüyü kır (Tek seferde bir birleştirme yeterli)
                break;
            } catch (Exception e) {
                // Hata olursa logla ama oyunu çökertme
                // printStackTrace yerine Logger kullanıldı.
                LOGGER.warn("Photonium: Failed to merge XP orbs.", e);
            }
        }
    }
}