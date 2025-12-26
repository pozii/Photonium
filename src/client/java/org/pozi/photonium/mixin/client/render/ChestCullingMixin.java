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

// HEDEF DEĞİŞİKLİĞİ: Genel dağıtıcı yerine doğrudan "ChestBlockEntityRenderer" (Sandık Çizici) sınıfını hedefliyoruz.
// Bu sınıf her sürümde mevcuttur ve hatasızdır.
@Mixin(ChestBlockEntityRenderer.class)
public class ChestCullingMixin {

    // Renderer sınıflarının render metodu genellikle "light" ve "overlay" parametrelerini de alır.
    // Bu yüzden parametre sayısını artırarak (int, int) tam eşleşme sağlıyoruz.
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

        // İLKEL MATEMATİK (Primitive Math)
        // Hata riskini sıfıra indirmek için yine manuel hesap yapıyoruz.
        double camX = camera.getX();
        double camY = camera.getY();
        double camZ = camera.getZ();

        BlockPos pos = entity.getPos();
        // Sandığın konumu
        double entX = pos.getX() + 0.5;
        double entY = pos.getY() + 0.5;
        double entZ = pos.getZ() + 0.5;

        // Mesafe Hesabı (Pisagor)
        double distSq = (camX - entX) * (camX - entX) +
                (camY - entY) * (camY - entY) +
                (camZ - entZ) * (camZ - entZ);

        // KARAR: 64 bloktan (4096 birim) uzaktaysa sandığı çizme!
        if (distSq > 4096.0D) {
            ci.cancel();
        }
    }
}