package org.pozi.photonium.mixin.client.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.pozi.photonium.util.PhotoniumLogger;

@Mixin(ItemEntity.class)
public abstract class FastItemSleepMixin {

    // itemAge sadece ItemEntity'ye özeldir, bu yüzden onu gölgelemekte sakınca yok.
    @Shadow private int itemAge;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTickFastSleep(CallbackInfo ci) {
        // TEKNİK DEĞİŞİKLİK: Gölgeleme (Shadow) Yerine Dönüştürme (Casting)
        // Mixin'in kendisini (this) bir 'Entity' nesnesi olarak tanıtıyoruz.
        // Bu sayede 'isOnGround', 'getVelocity' gibi ana metodlara doğrudan erişiyoruz.
        Entity self = (Entity) (Object) this;

        // 1. Güvenlik: Yer çekimi kontrolü ve hız kontrolü
        // self.isOnGround() -> Standart Entity metodu, hata vermez.
        // self.getVelocity() -> Standart Entity metodu, hata vermez.
        if (self.isOnGround() && self.getVelocity().lengthSquared() < 0.0001D) {

            // 2. Akıllı Uyku Döngüsü
            if (this.itemAge % 40 != 0) {

                // Yaşı manuel artır (Despawn için gerekli)
                this.itemAge++;

                // 5 Dakika (6000 tick) dolduysa sil.
                if (this.itemAge >= 6000) {
                    self.discard(); // self üzerinden discard çağırıyoruz.
                }

                PhotoniumLogger.logItemSleep();

                ci.cancel();
            }
        }
    }
}