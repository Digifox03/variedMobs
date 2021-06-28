package it.digifox03.variedmobs.props.item

import it.digifox03.variedmobs.RedirectContext
import it.digifox03.variedmobs.VariedMobs
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

@Serializable
@SerialName("${VariedMobs.modId}:context")
object ContextItemProp: ItemProp {
    override fun read(context: Map<Identifier, Any>): ItemStack {
        val item = context[RedirectContext.item]
        require(item is ItemStack) { "missing item from context" }
        return item
    }
}
