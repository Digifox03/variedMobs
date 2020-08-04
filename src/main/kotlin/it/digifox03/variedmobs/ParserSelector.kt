package it.digifox03.variedmobs

import com.google.gson.*
import net.minecraft.util.Identifier
import java.io.InputStream
import java.lang.reflect.Type


val selectors = mutableMapOf<Identifier, Class<out VariedSelector>>()

fun initSelectors() {
    selectors[ResultSelector.id]              = ResultSelector::class.java
    selectors[PickSelector.id]                = PickSelector::class.java
    selectors[SeqSelector.id]                 = SeqSelector::class.java
    selectors[BiomeSelector.id]               = BiomeSelector::class.java
    selectors[NameSelector.id]                = NameSelector::class.java
    selectors[BabySelector.id]                = BabySelector::class.java
    selectors[HealthSelector.id]              = HealthSelector::class.java
    selectors[CoordinateXSelector.id]         = CoordinateXSelector::class.java
    selectors[CoordinateYSelector.id]         = CoordinateYSelector::class.java
    selectors[CoordinateZSelector.id]         = CoordinateZSelector::class.java
    selectors[AgeSelector.id]                 = AgeSelector::class.java
    selectors[TimeSelector.id]                = TimeSelector::class.java
    selectors[WeatherSelector.id]             = WeatherSelector::class.java
    selectors[BiomeTemperatureSelector.id]    = BiomeTemperatureSelector::class.java
    selectors[BiomeRainfallSelector.id]       = BiomeRainfallSelector::class.java
    selectors[BiomeDepthSelector.id]          = BiomeDepthSelector::class.java
    selectors[SlotSelector.id]                = SlotSelector::class.java
    selectors[CMDSelector.id]                 = CMDSelector::class.java
    selectors[ItemDamageSelector.id]          = ItemDamageSelector::class.java
}

abstract class VariedSelector(val type: Identifier) {
    abstract fun choose(ctx: MutableMap<Identifier, Any>): Identifier?
}

object VariedSelectorSerializer : JsonSerializer<VariedSelector>, JsonDeserializer<VariedSelector> {
    override fun serialize(src: VariedSelector, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        context.serialize(src, typeOfSrc)

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): VariedSelector {
        return try {
            if (json.isJsonPrimitive && json.asJsonPrimitive.isString) {
                ResultSelector(context.deserialize(json, Identifier::class.java))
            } else {
                selectors[context.deserialize(json.asJsonObject["type"], Identifier::class.java)]?.let {
                    context.deserialize<VariedSelector>(json, it)
                } ?: ResultSelector(null)
            }
        } catch (err: JsonSyntaxException) {
            System.err.println(err.localizedMessage)
            ResultSelector(null)
        }
    }
}

object IdentifierSerializer : JsonSerializer<Identifier>, JsonDeserializer<Identifier> {
    override fun serialize(src: Identifier, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement =
        JsonPrimitive(src.toString())

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Identifier =
        Identifier.tryParse(json.asString)!!

}

fun parseSelector(input: InputStream): VariedSelector =
    GsonBuilder().runCatching {
        registerTypeAdapter(VariedSelector::class.java, VariedSelectorSerializer)
        registerTypeAdapter(Identifier::class.java, IdentifierSerializer)
        create().fromJson(String(input.readBytes()), VariedSelector::class.java)
    }.run {
        exceptionOrNull()?.printStackTrace()
        getOrNull() ?: ResultSelector(null)
    }
