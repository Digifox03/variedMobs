package it.digifox03.variedmobs.props.string

import it.digifox03.variedmobs.VariedMobs
import it.digifox03.variedmobs.props.ItemBasedProp
import it.digifox03.variedmobs.props.item.ItemProp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

@Serializable
@SerialName("${VariedMobs.modId}:item_type")
internal class ItemTypeStringProp(override val item: ItemProp?): ItemBasedProp<String>(), StringProp {
    override fun read(context: Map<Identifier, Any>): String {
        return Registry.ITEM.getId(getItem(context).item).toString()
    }
}
