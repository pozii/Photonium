package org.pozi.photonium.mixin.client.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.pozi.photonium.util.PhotoniumLogger;

@Mixin(Entity.class)
public abstract class EntityTickMixin {

    // Koordinatları almak için en temel yöntemleri gölgeliyoruz.
    // Bunlar her zaman 'public' olduğu için hata vermez.
    @Shadow public abstract double getX();
    @Shadow public abstract double getY();
    @Shadow public abstract double getZ();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        // Kendimizi (this) Entity olarak referans alıyoruz
        Entity self = (Entity) (Object) this;

        // 1. Kural: Oyuncuları asla dondurma.
        if (self instanceof PlayerEntity) {
            return;
        }

        // İstemci kontrolü: MinecraftClient üzerinden yapıyoruz.
        // Eğer oyun dünyasında değilsek (null ise) zaten işlem yapma.
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.getCameraEntity() == null) {
            return;
        }

        // 2. Kural: Mesafe Hesaplama (Manuel Matematik)
        // squaredDistanceTo metodu bazen hata verebildiği için el yordamıyla yapıyoruz.
        Entity camera = client.getCameraEntity();

        double dx = this.getX() - camera.getX();
        double dy = this.getY() - camera.getY();
        double dz = this.getZ() - camera.getZ();

        // Mesafe Karesi (a^2 + b^2 + c^2)
        double distanceSq = dx * dx + dy * dy + dz * dz;

        // 3. Kural: 64 Blok (4096 birim) uzaklıktaysa dondur.
        if (distanceSq > 4096.0D) {
            PhotoniumLogger.logEntityFreeze();
            ci.cancel();
        }
    }
}