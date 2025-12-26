package org.pozi.photonium.mixin.client.memory;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MemoryLeakFixMixin {
    @Inject(method = "init", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        System.gc();
    }
}
