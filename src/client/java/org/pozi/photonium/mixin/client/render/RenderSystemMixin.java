package org.pozi.photonium.mixin.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {
    /**
     * Oyun her karede "Render Thread'inde miyim?" diye kontrol eder.
     * Bu mixin, kontrol başlar başlamaz "Evet öylesin" diyerek işlemi iptal eder
     * ve işlemciyi boş yere yormaz.
     */
    @Inject(method = "assertOnRenderThread", at = @At("HEAD"), cancellable = true)
    private static void onAssertOnRenderThread(CallbackInfo ci) {
        ci.cancel();
    }
}