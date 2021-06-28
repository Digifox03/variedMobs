package it.digifox03.variedmobs.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object RegexSerializer: KSerializer<Regex> {
    override val descriptor = PrimitiveSerialDescriptor("regex", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Regex {
        return Regex(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Regex) {
        encoder.encodeString(value.pattern)
    }
}