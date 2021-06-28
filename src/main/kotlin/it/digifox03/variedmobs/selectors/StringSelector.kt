@file:UseSerializers(RegexSerializer::class)
package it.digifox03.variedmobs.selectors

import it.digifox03.variedmobs.VariedMobs
import it.digifox03.variedmobs.serializers.RegexSerializer
import it.digifox03.variedmobs.props.string.StringProp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import net.minecraft.util.Identifier

@Serializable
@SerialName("${VariedMobs.modId}:string")
class StringSelector(
    private val use: StringProp,
    private val options: ArrayList<StringOption>,
): Selector {
    @Serializable
    class StringOption(
        val regex: Regex? = null,
        val exact: String? = null,
        val use: Selector
    )

    override fun select(context: Map<Identifier, Any>): Identifier {
        val value = use.read(context)
        for (option in options) {
            if (option.regex?.containsMatchIn(value) == false) continue
            if (option.exact?.equals(value) == false) continue
            return option.use.select(context)
        }
        throw IllegalStateException("Missing default clause in selector")
    }
}
