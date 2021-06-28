package it.digifox03.variedmobs.selectors

import it.digifox03.variedmobs.VariedMobs
import it.digifox03.variedmobs.props.bool.BoolProp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.minecraft.util.Identifier

@Serializable
@SerialName("${VariedMobs.modId}:if")
class IfSelector(
    private val `if`: BoolProp,
    private val then: Selector,
    private val `else`: Selector
): Selector {
    override fun select(context: Map<Identifier, Any>): Identifier {
        return if (`if`.read(context)) {
            then.select(context)
        } else {
            `else`.select(context)
        }
    }
}
