package org.pozi.photonium.mixin.client.core;

import net.minecraft.client.MinecraftClient;
import org.pozi.photonium.util.PhotoniumLogger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class LoggerTickMixin {

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        PhotoniumLogger.tick();
    }
}
