package com.github.elenterius.blightlings.mixin;

import com.github.elenterius.blightlings.init.ModBiomes;
import net.minecraft.block.BlockState;
import net.minecraft.world.IWorld;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.NoiseChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin
{
    @Shadow
    @Final
    protected BlockState defaultFluid;

    @Shadow
    @Final
    private static BlockState AIR;

    private boolean chunkHasPrimaryBlightBiome = false;

    private boolean isBlightBiomeChunk() {
        return chunkHasPrimaryBlightBiome;
    }

    private void setBlightBiomeChunk(boolean flag) {
        chunkHasPrimaryBlightBiome = flag;
    }

    @Inject(method = "func_230352_b_", at = @At("HEAD"))
    protected void injectFunc_230352_b_(IWorld world, StructureManager manager, IChunk chunk, CallbackInfo ci) {
        setBlightBiomeChunk(false);
        if (chunk.getBiomes() != null) {
            int[] ids = Arrays.stream(chunk.getBiomes().getBiomeIds()).distinct().toArray();
            for (int id : ids) {
                if (id == ModBiomes.BLIGHT_BIOME_ID || id == ModBiomes.BLIGHT_BIOME_INNER_EDGE_ID) {
                    setBlightBiomeChunk(true);
                    break;
                }
            }
        }
    }

    @ModifyVariable(
            method = "func_230352_b_",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/gen/NoiseChunkGenerator;func_236086_a_(DI)Lnet/minecraft/block/BlockState;")
    )
    protected BlockState modifyBlockStateVariable(BlockState state, IWorld world, StructureManager manager, IChunk chunk) {
        if (isBlightBiomeChunk() && state == defaultFluid) return AIR;
        return state;
    }

}