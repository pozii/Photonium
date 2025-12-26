package org.pozi.photonium.mixin.client.render;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntityRenderer.class)
public class ChestCullingMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void onRender(
            BlockEntity entity,
            float tickDelta,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light,
            int overlay,
            CallbackInfo ci
    ) {
        MinecraftClient client = MinecraftClient.getInstance();
        Entity camera = client.getCameraEntity();

        if (camera == null) return;

        double camX = camera.getX();
        double camY = camera.getY();
        double camZ = camera.getZ();

        BlockPos pos = entity.getPos();
        
        double entX = pos.getX() + 0.5;
        double entY = pos.getY() + 0.5;
        double entZ = pos.getZ() + 0.5;

        double distSq = (camX - entX) * (camX - entX) +
                (camY - entY) * (camY - entY) +
                (camZ - entZ) * (camZ - entZ);

        if (distSq > 4096.0D) {
            ci.cancel();
        }
    }
}
