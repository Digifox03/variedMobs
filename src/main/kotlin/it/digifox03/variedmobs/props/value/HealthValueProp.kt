package it.digifox03.variedmobs.props.value

import it.digifox03.variedmobs.VariedMobs
import it.digifox03.variedmobs.props.EntityBasedProp
import it.digifox03.variedmobs.props.entity.EntityProp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.entity.LivingEntity
import net.minecraft.util.Identifier

@Serializable
@SerialName("${VariedMobs.modId}:health")
internal class HealthValueProp(override val entity: EntityProp? = null): EntityBasedProp<Float>(), ValueProp {
    override fun read(context: Map<Identifier, Any>): Float {
        val entity = getEntity(context)
        require(entity is LivingEntity)
        return entity.health
    }
}
