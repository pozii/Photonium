package org.pozi.photonium.mixin.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    public void shouldRender(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftClient.getInstance().getCameraEntity() != null) {
            // Varsayılan menzil: 64 Blok (Karesi 4096)
            double cutoffRange = 4096.0D;

            // EĞER varlık yerdeki bir eşya (Item) veya XP puanı ise menzili kıs:
            // 32 Blok (Karesi 1024)
            if (entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity) {
                cutoffRange = 1024.0D;
            }

            // Mesafe kontrolü
            if (entity.squaredDistanceTo(MinecraftClient.getInstance().getCameraEntity()) > cutoffRange) {
                cir.setReturnValue(false);
            }
        }
    }
}