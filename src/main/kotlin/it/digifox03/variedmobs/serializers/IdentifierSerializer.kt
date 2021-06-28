package it.digifox03.variedmobs.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.util.Identifier
import net.minecraft.util.InvalidIdentifierException

object IdentifierSerializer: KSerializer<Identifier> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Identifier", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Identifier {
        try {
            return Identifier(decoder.decodeString())
        } catch (e: InvalidIdentifierException) {
            throw SerializationException(e)
        }
    }

    override fun serialize(encoder: Encoder, value: Identifier) {
        encoder.encodeString(value.toString())
    }
}
