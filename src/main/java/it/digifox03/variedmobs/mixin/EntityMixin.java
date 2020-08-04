package it.digifox03.variedmobs.mixin;

import it.digifox03.variedmobs.user.VariedMobsEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements VariedMobsEntity {
    @Shadow
    public World world;

    @Shadow
    abstract public BlockPos getBlockPos();

    @Override
    public Biome getVariedMobs_spawnBiome() {
        return variedMobs_spawnBiome;
    }

    public Biome variedMobs_spawnBiome = null;

    @Inject(method = "baseTick()V", at = @At("HEAD"))
    public void baseTick(CallbackInfo ci) {
        if (variedMobs_spawnBiome == null) {
            variedMobs_spawnBiome = world.getBiome(getBlockPos());
        }
    }
}
