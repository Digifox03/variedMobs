package it.digifox03.variedmobs.core

import com.google.gson.*
import it.digifox03.variedmobs.api.Ctx
import it.digifox03.variedmobs.api.SelectorRegistry
import it.digifox03.variedmobs.selectors.*
import net.minecraft.util.Identifier
import java.io.InputStream
import java.lang.reflect.Type


object SelectorRegistryImpl : SelectorRegistry {
    val selectors = mutableMapOf<Identifier, Type>()

    override fun register(id: Identifier, selector: Type) {
        selectors[id] = selector
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

abstract class VariedSelector(val type: Identifier) {
    abstract fun choose(ctx: Ctx): Identifier?
    abstract fun validate(): String?
}
