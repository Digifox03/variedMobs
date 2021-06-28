package it.digifox03.variedmobs.props

import it.digifox03.variedmobs.props.entity.ContextEntityProp
import it.digifox03.variedmobs.props.entity.EntityProp
import kotlinx.serialization.Serializable
import net.minecraft.entity.Entity
import net.minecraft.util.Identifier

@Serializable
abstract class EntityBasedProp<T> : GenericProp<T> {
    protected abstract val entity: EntityProp?
    protected fun getEntity(context: Map<Identifier, Any>): Entity {
        return (entity ?: ContextEntityProp).read(context)
    }
}
