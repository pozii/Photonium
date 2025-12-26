package org.pozi.photonium.mixin.client.world;

import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientChunkManager.class)
public class ClientChunkManagerMixin {
    @Unique
    private WorldChunk photonium$cachedChunk;

    @Inject(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/WorldChunk;", at = @At("HEAD"), cancellable = true)
    private void getChunkFast(int x, int z, ChunkStatus status, boolean create, CallbackInfoReturnable<WorldChunk> cir) {
        if (photonium$cachedChunk != null && photonium$cachedChunk.getPos().x == x && photonium$cachedChunk.getPos().z == z && status == ChunkStatus.FULL) {
            cir.setReturnValue(photonium$cachedChunk);
        }
    }

    @Inject(method = "getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/WorldChunk;", at = @At("RETURN"))
    private void cacheChunk(int x, int z, ChunkStatus status, boolean create, CallbackInfoReturnable<WorldChunk> cir) {
        WorldChunk chunk = cir.getReturnValue();
        if (chunk != null && status == ChunkStatus.FULL) {
            photonium$cachedChunk = chunk;
        }
    }
}