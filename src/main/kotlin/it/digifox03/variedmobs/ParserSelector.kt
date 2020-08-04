package it.digifox03.variedmobs

import com.google.gson.*
import it.digifox03.variedmobs.api.SelectorRegistry
import net.minecraft.util.Identifier
import java.io.InputStream
import java.lang.reflect.Type


object SelectorRegistryImpl : SelectorRegistry {
    val selectors = mutableMapOf<Identifier, Type>()

    override fun register(id: Identifier, selector: Type) {
        register(id, selector)
    }
}

fun initSelectors() {
    SelectorRegistry.getInstance().apply {
        register(ResultSelector.id, ResultSelector::class.java)
        register(PickSelector.id, PickSelector::class.java)
        register(SeqSelector.id, SeqSelector::class.java)
        register(NotSelector.id, NotSelector::class.java)
        register(BiomeSelector.id, BiomeSelector::class.java)
        register(NameSelector.id, NameSelector::class.java)
        register(BabySelector.id, BabySelector::class.java)
        register(HealthSelector.id, HealthSelector::class.java)
        register(CoordinateXSelector.id, CoordinateXSelector::class.java)
        register(CoordinateYSelector.id, CoordinateYSelector::class.java)
        register(CoordinateZSelector.id, CoordinateZSelector::class.java)
        register(AgeSelector.id, AgeSelector::class.java)
        register(TimeSelector.id, TimeSelector::class.java)
        register(WeatherSelector.id, WeatherSelector::class.java)
        register(BiomeTemperatureSelector.id, BiomeTemperatureSelector::class.java)
        register(BiomeRainfallSelector.id, BiomeRainfallSelector::class.java)
        register(BiomeDepthSelector.id, BiomeDepthSelector::class.java)
        register(SlotSelector.id, SlotSelector::class.java)
        register(CMDSelector.id, CMDSelector::class.java)
        register(ItemDamageSelector.id, ItemDamageSelector::class.java)
    }
}

object VariedSelectorSerializer : JsonSerializer<VariedSelector>, JsonDeserializer<VariedSelector> {
    override fun serialize(src: VariedSelector, typeOfSrc: Type, context: JsonSerializationContext): JsonElement =
        context.serialize(src, typeOfSrc)

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext): VariedSelector {
        return try {
            if (json.isJsonPrimitive && json.asJsonPrimitive.isString) {
                ResultSelector(context.deserialize(json, Identifier::class.java))
            } else {
                SelectorRegistryImpl.selectors[context.deserialize(json.asJsonObject["type"], Identifier::class.java)]?.let {
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
