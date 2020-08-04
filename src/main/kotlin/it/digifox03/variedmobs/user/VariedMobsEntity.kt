package it.digifox03.variedmobs.user

import net.minecraft.world.biome.Biome

interface VariedMobsEntity {
    @Suppress("PropertyName")
    val variedMobs_spawnBiome : Biome?
}