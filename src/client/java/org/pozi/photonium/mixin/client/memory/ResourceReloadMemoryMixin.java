package org.pozi.photonium.mixin.client.memory;

import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class ResourceReloadMemoryMixin {
    @Inject(method = "reload()V", at = @At("RETURN"))
    private void onReload(CallbackInfo ci) {
        System.gc();
    }
}
