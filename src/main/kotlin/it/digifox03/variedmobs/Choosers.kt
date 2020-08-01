package it.digifox03.variedmobs

import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import kotlin.math.abs
import kotlin.random.Random

typealias Ctx = MutableMap<Identifier, Any>

val LivingEntity.biome
    get() = world.getBiome(blockPos)

val Ctx.entity
    get() = this[Identifier(MODID, "entity")] as? LivingEntity ?: error("missing entity")

val Ctx.random
    get() = this.getOrPut(Identifier(MODID, "random")) { Random(entity.uuid.leastSignificantBits) } as Random

fun Ctx.clone() = toMap().toMutableMap()


class ResultChooser(var result : Identifier?) : VariedChooser(id) {
    companion object { val id = Identifier(MODID, "result") }
    override fun choose(ctx: MutableMap<Identifier, Any>): Identifier? = result
}

class PickChooser(var weights : List<Double>, var choices : List<VariedChooser>) : VariedChooser(id) {
    companion object { val id = Identifier(MODID, "pick") }
    override fun choose(ctx: Ctx): Identifier? =
            select(choices.zip(weights).toMutableList(), ctx)

    private tailrec fun select(opts: MutableList<Pair<VariedChooser, Double>>, ctx: Ctx): Identifier? =
            if (opts.isEmpty()) {
                null
            } else {
                opts.randomWPop(ctx.random)?.choose(ctx.clone()) ?: select(opts, ctx)
            }
}

class SeqChooser(var choices: List<VariedChooser>) : VariedChooser(id) {
    companion object { val id = Identifier(MODID, "seq") }

    override fun choose(ctx: Ctx): Identifier? {
        for (c in choices) {
            val res = c.choose(ctx.clone())
            if (res != null) {
                return res
            }
        }
        return null
    }
}

abstract class BoolChooser(type: Identifier, var value: VariedChooser) : VariedChooser(type) {
    override fun choose(ctx: Ctx): Identifier? =
        if (prop(ctx.clone())) value.choose(ctx.clone()) else null

    abstract fun prop(ctx: Ctx): Boolean
}

class BiomeChooser(var biome: List<Identifier>, value: VariedChooser) : BoolChooser(id, value) {
    companion object { val id = Identifier(MODID, "biome") }
    override fun prop(ctx: Ctx): Boolean =
        Registry.BIOME.getId(ctx.entity.biome) in biome
}

class NameChooser(regex: String, value: VariedChooser) : BoolChooser(id, value) {
    companion object { val id = Identifier(MODID, "name") }
    @Transient val reg: Regex = Regex(regex)
    override fun prop(ctx: Ctx): Boolean =
        ctx.entity.customName?.let { reg.containsMatchIn(ctx.toString()) } == true
}

class BabyChooser(value: VariedChooser) : BoolChooser(id, value) {
    companion object { val id = Identifier(MODID, "baby") }
    override fun prop(ctx: Ctx): Boolean = ctx.entity.isBaby
}

abstract class BoundedPropChooser(
    type: Identifier,
    private val positions: List<Double>,
    private val weights: List<Double>?,
    private val choices: List<VariedChooser>
) : VariedChooser(type) {
    abstract fun getter(ctx: Ctx): Double
    override fun choose(ctx: Ctx): Identifier? {
        val center = getter(ctx.clone())
        return when {
            weights != null -> {
                positions
                    .zip(weights)
                    .map { (v, w) -> abs(center - v) / w }
                    .let { it.indexOf(it.min()) }
                    .let { choices.getOrNull(it)  }
            }
            else -> {
                positions
                    .zipWithNext()
                    .indexOfFirst { (min, max) -> center in min..max }
                    .let { choices.getOrNull(it) }
            }
        }?.choose(ctx.clone())
    }
}


class HealthChooser(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedChooser>
) : BoundedPropChooser(id, positions, weights, choices) {
     companion object { val id = Identifier(MODID, "health-prop") }
    override fun getter(ctx: Ctx) = (ctx.entity.health / ctx.entity.maxHealth).toDouble()
}

class CoordinateYChooser(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedChooser>
) : BoundedPropChooser(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "y-prop") }
    override fun getter(ctx: Ctx) = ctx.entity.y
}

class CoordinateXChooser(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedChooser>
) : BoundedPropChooser(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "x-prop") }
    override fun getter(ctx: Ctx) = ctx.entity.x
}

class CoordinateZChooser(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedChooser>
) : BoundedPropChooser(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "z-prop") }
    override fun getter(ctx: Ctx) = ctx.entity.z
}

class AgeChooser(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedChooser>
) : BoundedPropChooser(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "age-prop") }
    override fun getter(ctx: Ctx) = ctx.entity.age.toDouble()
}

class TimeChooser(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedChooser>
) : BoundedPropChooser(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "time-prop") }
    override fun getter(ctx: Ctx) = ctx.entity.world.timeOfDay.toDouble()
}

class WeatherChooser(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedChooser>
) : BoundedPropChooser(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "age-prop") }
    override fun getter(ctx: Ctx) = when {
        ctx.entity.world.isThundering -> 2.0
        ctx.entity.world.isRaining    -> 1.0
        else                          -> 0.0
    }
}

class BiomeTemperatureChooser(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedChooser>
) : BoundedPropChooser(WeatherChooser.id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "biome-temperature-prop") }
    override fun getter(ctx: Ctx): Double = ctx.entity.biome.temperature.toDouble()
}

class BiomeRainfallChooser(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedChooser>
) : BoundedPropChooser(WeatherChooser.id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "biome-rainfall-prop") }
    override fun getter(ctx: Ctx): Double = ctx.entity.biome.rainfall.toDouble()
}

class BiomeDepthChooser(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedChooser>
) : BoundedPropChooser(WeatherChooser.id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "biome-depth-prop") }
    override fun getter(ctx: Ctx): Double = ctx.entity.biome.depth.toDouble()
}
