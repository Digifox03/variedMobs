package it.digifox03.variedmobs.selectors

import it.digifox03.variedmobs.VariedMobs
import it.digifox03.variedmobs.props.value.ValueProp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.util.Identifier

@Serializable
@SerialName("${VariedMobs.modId}:range")
class RangeSelector(
    private val use: ValueProp,
    private val options: ArrayList<RangeOption>
): Selector {
    @Serializable
    class RangeOption(
        val above: Float? = null,
        val aboveOrEqual: Float? = null,
        val below: Float? = null,
        val belowOrEqual: Float? = null,
        val use: Selector
    )

    override fun select(context: Map<Identifier, Any>): Identifier {
        val value = use.read(context)
        for (option in options) {
            if (option.above?.let { value > it } == false) continue
            if (option.aboveOrEqual?.let { value >= it } == false) continue
            if (option.below?.let { value < it } == false) continue
            if (option.belowOrEqual?.let { value <= it } == false) continue
            return option.use.select(context)
        }
        throw IllegalStateException("Missing default clause in selector")
    }

    override val valid: Boolean
        get() = true // TODO: implement range checking
}
