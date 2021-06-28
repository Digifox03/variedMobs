package it.digifox03.variedmobs

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.plus
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.resource.ResourceManagerHelper
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.ResourceType
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.InputStream
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

object VariedMobs: ClientModInitializer {
    const val modId = "varied-mobs"
    private const val resPrefix = "varied"
    private val resourceId = Identifier(modId, "selector")
    private lateinit var redirect: Map<Identifier, SelectorRoot>
    private lateinit var json: Json
    private val logger: Logger = LogManager.getLogger("varied-mobs")

    private object ReloadListener: SimpleResourceReloadListener<Map<Identifier, SelectorRoot>> {
        override fun getFabricId() = resourceId

        private fun fixId(id: Identifier) =
            Identifier(
                id.namespace,
                id.path
                    .removePrefix("$resPrefix/")
                    .removeSuffix(".json")
            )

        private fun loadSelector(id: Identifier, input: InputStream): SelectorRoot {
            return try {
                json.decodeFromString<SelectorRoot>(input.bufferedReader().readText()).also {
                    require(it.version == 1) { "unsupported version ${it.version}" }
                }
            } catch (e: IllegalArgumentException) {
                logger.error("malformed input $id: ${e.message}")
                throw e
            }
        }

        override fun load(manager: ResourceManager, profiler: Profiler, executor: Executor) =
            CompletableFuture.supplyAsync({
                manager.findResources(resPrefix) { true }.associate { id ->
                    manager.getResource(id).inputStream.use { input ->
                        id.let { fixId(it) to loadSelector(it, input) }
                    }
                }
            }, executor)!!

        override fun apply(
            data: Map<Identifier, SelectorRoot>,
            manager: ResourceManager,
            profiler: Profiler,
            executor: Executor
        ): CompletableFuture<Void> = CompletableFuture.runAsync({
            redirect = data
        }, executor)
    }

    override fun onInitializeClient() {
        ResourceManagerHelper
            .get(ResourceType.CLIENT_RESOURCES)
            .registerReloadListener(ReloadListener)

        json = Json {
            serializersModule = FabricLoader.getInstance()
                .getEntrypoints("varied-mobs", VariedMobsSelectorRegister::class.java)
                .map(VariedMobsSelectorRegister::module)
                .reduce { acc, serializersModule -> acc + serializersModule }
        }
    }

    fun redirect(identifier: Identifier, data: Map<Identifier, Any>): Identifier {
        return redirect[identifier]?.root?.select(data) ?: identifier
    }

    @JvmStatic
    fun redirectTexture(identifier: Identifier, data: Map<Identifier, Any>): Identifier {
        return redirect(Identifier(identifier.namespace, identifier.path.removeSuffix(".png")), data)
    }
}
