package it.digifox03.variedmobs.props.bool

import it.digifox03.variedmobs.VariedMobs
import it.digifox03.variedmobs.props.EntityBasedProp
import it.digifox03.variedmobs.props.entity.EntityProp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.entity.passive.FoxEntity
import net.minecraft.util.Identifier

@Serializable
@SerialName("${VariedMobs.modId}:fox_is_sleeping")
internal class FoxIsSleepingBoolProp(override val entity: EntityProp? = null) : EntityBasedProp<Boolean>(), BoolProp {
    override fun read(context: Map<Identifier, Any>): Boolean {
        val entity = getEntity(context)
        require(entity is FoxEntity)
        return entity.isSleeping
    }
}
