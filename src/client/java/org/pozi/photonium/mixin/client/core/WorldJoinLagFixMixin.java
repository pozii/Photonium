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
    @Inject(method = "onGameJoin", at = @At("RETURN"))
    private void onJoinWorld(GameJoinS2CPacket packet, CallbackInfo ci) {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

        for (Thread t : threadSet) {
            String name = t.getName().toLowerCase();
            if (name.contains("worker") || name.contains("io") || name.contains("chunk")) {
                try {
                    t.setPriority(3);
                } catch (Exception ignored) {
                }
            }
        }

        System.gc();
    }
}
