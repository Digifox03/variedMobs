package it.digifox03.variedmobs.props

import it.digifox03.variedmobs.props.item.ContextItemProp
import it.digifox03.variedmobs.props.item.ItemProp
import kotlinx.serialization.Serializable
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

@Serializable
abstract class ItemBasedProp<T> : GenericProp<T> {
    protected abstract val item: ItemProp?
    protected fun getItem(context: Map<Identifier, Any>): ItemStack {
        return (item ?: ContextItemProp).read(context)
    }
}
