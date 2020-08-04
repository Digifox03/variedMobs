package it.digifox03.variedmobs.api

import it.digifox03.variedmobs.MODID
import net.minecraft.entity.Entity
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier
import kotlin.random.Random

typealias Ctx = MutableMap<Identifier, Any>

val Ctx.entity
    get() = this[CtxId.entity] as? Entity ?: error("missing entity")
val Ctx.livingEntity
    get() = this[CtxId.entity] as? LivingEntity ?: error("missing entity")
val Ctx.slot
    get() = this[CtxId.slot] as? EquipmentSlot
val Ctx.random
    get() = this.getOrPut(CtxId.random) { Random(entity.uuid.leastSignificantBits) } as Random
fun Ctx.clone() = toMap().toMutableMap()

object CtxId {
    val entity = Identifier(MODID, "entity")
    val slot = Identifier(MODID, "slot")
    val random = Identifier(MODID, "random")
}
