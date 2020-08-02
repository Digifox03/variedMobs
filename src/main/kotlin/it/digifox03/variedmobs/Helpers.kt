package it.digifox03.variedmobs

import net.minecraft.util.profiler.Profiler
import net.minecraft.world.biome.Biome
import kotlin.random.Random

fun <T> Profiler.profiling(block: () -> T): T = startTick().run { block() }.also { endTick() }
fun <T> MutableList<Pair<T, Double>>.randomWPop(random: Random): T? {
    var choice = random.nextDouble(.0, fold(.0) {a, (_, b) -> a + b})
    val res = indexOfFirst { (_, b) -> choice -= b; choice <= 0 }
    return getOrNull(res)?.first.also { removeAt(res) }
}

interface VariedMobsLivingEntity {
    @Suppress("PropertyName")
    val variedMobs_spawnBiome : Biome?
}
