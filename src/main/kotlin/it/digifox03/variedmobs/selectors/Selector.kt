package it.digifox03.variedmobs.selectors

import it.digifox03.variedmobs.user.VariedMobsEntity
import it.digifox03.variedmobs.core.VariedSelector
import it.digifox03.variedmobs.api.*
import it.digifox03.variedmobs.core.MODID
import net.minecraft.entity.Entity
import net.minecraft.util.Identifier
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import kotlin.math.abs

@Suppress("unused")
fun init() {
    SelectorRegistry.getInstance().apply {
        register(ResultSelector.id, ResultSelector::class.java)
        register(PickSelector.id, PickSelector::class.java)
        register(SeqSelector.id, SeqSelector::class.java)
        register(NotSelector.id, NotSelector::class.java)
        register(BiomeSelector.id, BiomeSelector::class.java)
        register(NameSelector.id, NameSelector::class.java)
        register(BabySelector.id, BabySelector::class.java)
        register(HealthSelector.id, HealthSelector::class.java)
        register(CoordinateXSelector.id, CoordinateXSelector::class.java)
        register(CoordinateYSelector.id, CoordinateYSelector::class.java)
        register(CoordinateZSelector.id, CoordinateZSelector::class.java)
        register(AgeSelector.id, AgeSelector::class.java)
        register(TimeSelector.id, TimeSelector::class.java)
        register(WeatherSelector.id, WeatherSelector::class.java)
        register(BiomeTemperatureSelector.id, BiomeTemperatureSelector::class.java)
        register(BiomeRainfallSelector.id, BiomeRainfallSelector::class.java)
        register(BiomeDepthSelector.id, BiomeDepthSelector::class.java)
        register(SlotSelector.id, SlotSelector::class.java)
        register(CMDSelector.id, CMDSelector::class.java)
        register(ItemDamageSelector.id, ItemDamageSelector::class.java)
    }
}

val Entity.biome: Biome
    get() = (this as VariedMobsEntity).variedMobs_spawnBiome ?: world.getBiome(blockPos)

class ResultSelector(private var result : Identifier?) : VariedSelector(id) {
    companion object { val id = Identifier(MODID, "result") }
    override fun choose(ctx: MutableMap<Identifier, Any>): Identifier? = result
}

class PickSelector(private var weights : List<Double>?, private var choices : List<VariedSelector>) : VariedSelector(id) {
    companion object { val id = Identifier(MODID, "pick") }
    override fun choose(ctx: Ctx): Identifier? =
            select(choices.zip(weights ?: generateSequence { 1.0 }.asIterable()).toMutableList(), ctx)

    private tailrec fun select(opts: MutableList<Pair<VariedSelector, Double>>, ctx: Ctx): Identifier? =
            if (opts.isEmpty()) {
                null
            } else {
                opts.randomWPop(ctx.random)?.choose(ctx.clone()) ?: select(opts, ctx)
            }
}

class SeqSelector(private var choices: List<VariedSelector>) : VariedSelector(id) {
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

class NotSelector(private var value: VariedSelector) : VariedSelector(id) {
    companion object { val id = Identifier(MODID, "not") }

    override fun choose(ctx: Ctx): Identifier? =
            value.choose(ctx.clone()).let {
                if (it?.path == "") {
                    null
                } else {
                    it
                }
            }
}

abstract class BoolSelector(type: Identifier, private var value: VariedSelector) : VariedSelector(type) {
    override fun choose(ctx: Ctx): Identifier? =
        if (prop(ctx.clone())) value.choose(ctx.clone()) else null

    abstract fun prop(ctx: Ctx): Boolean
}

class BiomeSelector(private var biome: List<Identifier>, value: VariedSelector) : BoolSelector(id, value) {
    companion object { val id = Identifier(MODID, "biome") }
    override fun prop(ctx: Ctx): Boolean =
        BuiltinRegistries.BIOME.getId(ctx.entity.biome) in biome
}

class NameSelector(regex: String, value: VariedSelector) : BoolSelector(id, value) {
    companion object { val id = Identifier(MODID, "name") }
    @Transient val reg: Regex = Regex(regex)
    override fun prop(ctx: Ctx): Boolean =
        ctx.entity.customName?.let { reg.containsMatchIn(ctx.toString()) } == true
}

class BabySelector(value: VariedSelector) : BoolSelector(id, value) {
    companion object { val id = Identifier(MODID, "baby") }
    override fun prop(ctx: Ctx): Boolean = ctx.livingEntity.isBaby
}

abstract class BoundedPropSelector(
    type: Identifier,
    private val positions: List<Double>,
    private val weights: List<Double>?,
    private val choices: List<VariedSelector>
) : VariedSelector(type) {
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
                    .indexOfFirst { (min, max) -> min >= center && center < max }
                    .let { choices.getOrNull(it) }
            }
        }?.choose(ctx.clone())
    }
}

class SlotSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "slot-prop") }
    override fun getter(ctx: Ctx) = ctx.slot?.armorStandSlotId?.toDouble() ?: 0.0
}

class CMDSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "cmd-prop") }
    override fun getter(ctx: Ctx) = ctx
        .slot?.let { ctx.livingEntity.getEquippedStack(it) }
        ?.tag?.getInt("CustomModelData")?.toDouble() ?: .0
}

class ItemDamageSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "item-damage-prop") }
    override fun getter(ctx: Ctx) = ctx
        .slot?.let { ctx.livingEntity.getEquippedStack(it) }
        ?.let { it.damage.toDouble() / it.maxDamage.toDouble() }
        ?: 0.0
}

class HealthSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(id, positions, weights, choices) {
     companion object { val id = Identifier(MODID, "health-prop") }
    override fun getter(ctx: Ctx) = (ctx.livingEntity.health / ctx.livingEntity.maxHealth).toDouble()
}

class CoordinateYSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "y-prop") }
    override fun getter(ctx: Ctx) = ctx.entity.y
}

class CoordinateXSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "x-prop") }
    override fun getter(ctx: Ctx) = ctx.entity.x
}

class CoordinateZSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "z-prop") }
    override fun getter(ctx: Ctx) = ctx.entity.z
}

class AgeSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "age-prop") }
    override fun getter(ctx: Ctx) = ctx.entity.age.toDouble()
}

class TimeSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "time-prop") }
    override fun getter(ctx: Ctx) = ctx.entity.world.timeOfDay.toDouble()
}

class WeatherSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "weather-prop") }
    override fun getter(ctx: Ctx) = when {
        ctx.entity.world.isThundering -> 2.0
        ctx.entity.world.isRaining    -> 1.0
        else                          -> 0.0
    }
}

class BiomeTemperatureSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(WeatherSelector.id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "biome-temperature-prop") }
    override fun getter(ctx: Ctx): Double = ctx.entity.biome.temperature.toDouble()
}

class BiomeRainfallSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(WeatherSelector.id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "biome-rainfall-prop") }
    override fun getter(ctx: Ctx): Double = ctx.entity.biome.downfall.toDouble()
}

class BiomeDepthSelector(
    positions: List<Double>, weights: List<Double>?, choices: List<VariedSelector>
) : BoundedPropSelector(WeatherSelector.id, positions, weights, choices) {
    companion object { val id = Identifier(MODID, "biome-depth-prop") }
    override fun getter(ctx: Ctx): Double = ctx.entity.biome.depth.toDouble()
}

