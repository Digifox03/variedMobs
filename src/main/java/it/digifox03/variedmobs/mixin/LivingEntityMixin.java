package it.digifox03.variedmobs.mixin;

import it.digifox03.variedmobs.VariedMobsKt;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public Biome variedMobs_spawnBiome = null;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "baseTick()V", at = @At("HEAD"))
    public void baseTick(CallbackInfo ci) {
        if (!this.world.isClient && variedMobs_spawnBiome == null) {
            variedMobs_spawnBiome = world.getBiome(getBlockPos());
        }
    }

    @Inject(method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"))
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains(VariedMobsKt.getMODID() + ":SpawnBiome")) {
            variedMobs_spawnBiome = Registry.BIOME.get(
                    Identifier.tryParse(tag.getString(VariedMobsKt.getMODID() + ":SpawnBiome"))
            );
        }
    }

    @Inject(method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"))
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
        String name = "";
        if (variedMobs_spawnBiome != null) {
            Identifier id = Registry.BIOME.getId(variedMobs_spawnBiome);
            if (id != null) {
                name = id.toString();
            }
        }
        tag.putString(VariedMobsKt.getMODID() + ":SpawnBiome", name);
    }
}
