package org.pozi.photonium.mixin.client.core;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(ClientPlayNetworkHandler.class)
public class WorldJoinLagFixMixin {

    /**
     * Oyuncu bir dünyaya veya sunucuya bağlandığında (onGameJoin) çalışır.
     * Amacı: Arka plandaki "Chunk Oluşturucu" işçilerin önceliğini düşürmek.
     */
    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onJoinWorld(GameJoinS2CPacket packet, CallbackInfo ci) {
        // Sistemdeki tüm çalışan iş parçacıklarını (Thread) tarıyoruz.
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

        for (Thread t : threadSet) {
            String name = t.getName().toLowerCase();

            // Eğer iş parçacığı "Worker" (İşçi), "IO" (Veri) veya "Chunk" ile alakalıysa:
            if (name.contains("worker") || name.contains("io") || name.contains("chunk")) {

                // Normalde öncelik 5'tir (NORM_PRIORITY).
                // Biz bunu 3'e çekiyoruz. (MIN_PRIORITY 1'dir).
                // Bu sayede İşlemci, oyunu çizen ana ekrana (Render Thread) saygı gösterir.
                try {
                    t.setPriority(3);
                } catch (Exception ignored) {
                    // Güvenlik: Bazı sistem threadlerine dokunulamaz, hata verirse pas geç.
                }
            }
        }

        // Giriş anında temiz bir sayfa için RAM temizliği de yapalım.
        System.gc();
    }
}