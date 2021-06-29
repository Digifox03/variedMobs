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
    private val `when`: StringProp,
    private val options: ArrayList<StringOption>,
): Selector {
    @Serializable
    class StringOption(
        val containPattern: Regex? = null,
        val doesNotContainPattern: Regex? = null,
        val matchExactly: String? = null,
        val doesNotMatchExactly: String? = null,
        val then: Selector
    )

    override fun select(context: Map<Identifier, Any>): Identifier {
        val value = `when`.read(context)
        for (option in options) {
            if (option.containPattern?.containsMatchIn(value) == false) continue
            if (option.matchExactly?.equals(value) == false) continue
            if (option.doesNotContainPattern?.containsMatchIn(value) == true) continue
            if (option.doesNotMatchExactly?.equals(value) == true) continue
            return option.then.select(context)
        }
        throw IllegalStateException("Missing default clause in selector")
    }
}
