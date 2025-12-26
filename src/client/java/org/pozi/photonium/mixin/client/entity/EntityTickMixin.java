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

    @Shadow public abstract double getX();
    @Shadow public abstract double getY();
    @Shadow public abstract double getZ();

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self instanceof PlayerEntity) {
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.getCameraEntity() == null) {
            return;
        }
        Entity camera = client.getCameraEntity();

        double dx = this.getX() - camera.getX();
        double dy = this.getY() - camera.getY();
        double dz = this.getZ() - camera.getZ();
        double distanceSq = dx * dx + dy * dy + dz * dz;
        if (distanceSq > 4096.0D) {
            PhotoniumLogger.logEntityFreeze();
            ci.cancel();
        }
    }
}
