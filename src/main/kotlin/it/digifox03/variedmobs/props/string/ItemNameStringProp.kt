package it.digifox03.variedmobs.props.string

import it.digifox03.variedmobs.VariedMobs
import it.digifox03.variedmobs.props.ItemBasedProp
import it.digifox03.variedmobs.props.item.ItemProp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.util.Identifier

@Serializable
@SerialName("${VariedMobs.modId}:item-name")
internal class ItemNameStringProp(override val item: ItemProp?): ItemBasedProp<String>(), StringProp {
    override fun read(context: Map<Identifier, Any>): String {
        return getItem(context).name.asString()
    }
}
