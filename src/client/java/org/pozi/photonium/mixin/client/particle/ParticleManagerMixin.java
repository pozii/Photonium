package org.pozi.photonium.mixin.client.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
    public void addParticle(Particle particle, CallbackInfo ci) {
        Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();
        if (cameraEntity != null) {
            // getX(), getY(), getZ() kullanımı sürüm bağımsız en güvenli yoldur.
            double x = particle.getBoundingBox().getCenter().x;
            double y = particle.getBoundingBox().getCenter().y;
            double z = particle.getBoundingBox().getCenter().z;

            if (cameraEntity.squaredDistanceTo(x, y, z) > 1024.0D) { // 32 Blok mesafe sınırı
                ci.cancel();
            }
        }
    }
}