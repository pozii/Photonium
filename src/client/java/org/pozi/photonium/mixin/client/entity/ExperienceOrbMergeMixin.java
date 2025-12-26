package org.pozi.photonium.mixin.client.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.util.math.Box;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.pozi.photonium.util.PhotoniumLogger;

import java.lang.reflect.Field;
import java.util.List;

@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbMergeMixin {

    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("Photonium");

    @Unique
    private static Field AMOUNT_FIELD;
    static {
        try {
            for (Field f : ExperienceOrbEntity.class.getDeclaredFields()) {
                if (f.getType() == int.class) {
                    f.setAccessible(true);
                    AMOUNT_FIELD = f;
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Photonium: XP Amount field not found via reflection!", e);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ExperienceOrbEntity self = (ExperienceOrbEntity) (Object) this;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null) return;
        if (Math.random() > 0.05) return;

        mergeNearbyOrbs(self, client);
    }

    @Unique
    private void mergeNearbyOrbs(ExperienceOrbEntity self, MinecraftClient client) {
        if (AMOUNT_FIELD == null) return;
        Box searchBox = self.getBoundingBox().expand(0.5D);

        List<ExperienceOrbEntity> nearbyOrbs = client.world.getEntitiesByClass(
                ExperienceOrbEntity.class,
                searchBox,
                entity -> entity != self && !entity.isRemoved()
        );

        for (ExperienceOrbEntity otherOrb : nearbyOrbs) {
            try {
                int myAmount = AMOUNT_FIELD.getInt(self);
                int otherAmount = AMOUNT_FIELD.getInt(otherOrb);

                int newAmount = myAmount + otherAmount;

                AMOUNT_FIELD.setInt(self, newAmount);

                otherOrb.remove(Entity.RemovalReason.DISCARDED);
                PhotoniumLogger.logXpMerge();

                break;
            } catch (Exception e) {

                LOGGER.warn("Photonium: Failed to merge XP orbs.", e);
            }
        }
    }
}
