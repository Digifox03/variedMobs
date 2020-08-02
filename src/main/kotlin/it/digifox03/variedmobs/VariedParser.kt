package it.digifox03.variedmobs

import com.google.gson.*
import net.minecraft.util.Identifier
import java.io.InputStream
import java.lang.reflect.Type


val choosers = mutableMapOf<Identifier, Class<out VariedChooser>>()

fun initChoosers() {
    choosers[ResultChooser.id]              = ResultChooser::class.java
    choosers[PickChooser.id]                = PickChooser::class.java
    choosers[SeqChooser.id]                 = SeqChooser::class.java
    choosers[BiomeChooser.id]               = BiomeChooser::class.java
    choosers[NameChooser.id]                = NameChooser::class.java
    choosers[BabyChooser.id]                = BabyChooser::class.java
    choosers[HealthChooser.id]              = HealthChooser::class.java
    choosers[CoordinateXChooser.id]         = CoordinateXChooser::class.java
    choosers[CoordinateYChooser.id]         = CoordinateYChooser::class.java
    choosers[CoordinateZChooser.id]         = CoordinateZChooser::class.java
    choosers[AgeChooser.id]                 = AgeChooser::class.java
    choosers[TimeChooser.id]                = TimeChooser::class.java
    choosers[WeatherChooser.id]             = WeatherChooser::class.java
    choosers[BiomeTemperatureChooser.id]    = BiomeTemperatureChooser::class.java
    choosers[BiomeRainfallChooser.id]       = BiomeRainfallChooser::class.java
    choosers[BiomeDepthChooser.id]          = BiomeDepthChooser::class.java
}

abstract class VariedChooser(val type: Identifier) {
    abstract fun choose(ctx: MutableMap<Identifier, Any>): Identifier?
}

object VariedChooserSerializer : JsonSerializer<VariedChooser>, JsonDeserializer<VariedChooser> {
    override fun serialize(src: VariedChooser, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        context.serialize(src, typeOfSrc)

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): VariedChooser {
        return try {
            if (json.isJsonPrimitive && json.asJsonPrimitive.isString) {
                ResultChooser(context.deserialize(json, Identifier::class.java))
            } else {
                choosers[context.deserialize(json.asJsonObject["type"], Identifier::class.java)]?.let {
                    context.deserialize<VariedChooser>(json, it)
                } ?: ResultChooser(null)
            }
        } catch (err: JsonSyntaxException) {
            System.err.println(err.localizedMessage)
            ResultChooser(null)
        }
    }
}

object IdentifierSerializer : JsonSerializer<Identifier>, JsonDeserializer<Identifier> {
    override fun serialize(src: Identifier, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement =
        JsonPrimitive(src.toString())

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Identifier =
        Identifier.tryParse(json.asString)!!

}

fun parseChooser(input: InputStream): VariedChooser =
    GsonBuilder().runCatching {
        registerTypeAdapter(VariedChooser::class.java, VariedChooserSerializer)
        registerTypeAdapter(Identifier::class.java, IdentifierSerializer)
        create().fromJson(String(input.readBytes()), VariedChooser::class.java)
    }.run {
        exceptionOrNull()?.printStackTrace()
        getOrNull() ?: ResultChooser(null)
    }
