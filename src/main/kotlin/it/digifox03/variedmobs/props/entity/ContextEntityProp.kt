package it.digifox03.variedmobs.props.entity

import it.digifox03.variedmobs.RedirectContext
import it.digifox03.variedmobs.VariedMobs
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.entity.Entity
import net.minecraft.util.Identifier

@Serializable
@SerialName("${VariedMobs.modId}:context")
object ContextEntityProp: EntityProp {
    override fun read(context: Map<Identifier, Any>): Entity {
        val entity = context[RedirectContext.entity]
        require(entity is Entity) { "missing entity from context" }
        return entity
    }
}
