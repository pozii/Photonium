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
    @Shadow private int itemAge;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTickFastSleep(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        if (self.isOnGround() && self.getVelocity().lengthSquared() < 0.0001D) {
            if (this.itemAge % 40 != 0) {
                this.itemAge++;
                if (this.itemAge >= 6000) {
                    self.discard();
                }

                PhotoniumLogger.logItemSleep();

                ci.cancel();
            }
        }
    }
}
