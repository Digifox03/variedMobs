package it.digifox03.variedmobs.mixin;

import it.digifox03.variedmobs.VariedMobsLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements VariedMobsLivingEntity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

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
