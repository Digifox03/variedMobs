@file:UseSerializers(IdentifierSerializer::class)
package it.digifox03.variedmobs.selectors

import it.digifox03.variedmobs.serializers.IdentifierSerializer
import it.digifox03.variedmobs.VariedMobs
import kotlinx.serialization.SerialName
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.Serializable
import net.minecraft.util.Identifier

@Serializable
@SerialName("${VariedMobs.modId}:constant")
class ConstantSelector(private val identifier: Identifier): Selector {
    override fun select(context: Map<Identifier, Any>): Identifier = identifier
}
