package it.digifox03.variedmobs

import net.minecraft.entity.LivingEntity
import net.minecraft.resource.Resource
import net.minecraft.resource.ResourceManager
import net.minecraft.resource.SinglePreparationResourceReloadListener
import net.minecraft.util.Identifier
import net.minecraft.util.profiler.Profiler

const val MODID = "varied-mobs"

fun <T> Profiler.profiling(block: () -> T): T = startTick().run { block() }.also { endTick() }

@Suppress("unused")
fun init() {
    initChoosers()
}

object VariedMobManager : SinglePreparationResourceReloadListener<Map<Identifier, VariedChooser>>() {
    private lateinit var redirectMap: Map<Identifier, VariedChooser>

    fun redirectTexture(id: Identifier, le: LivingEntity, ctx: Ctx?): Identifier {
        val ctx1 = ctx ?: mutableMapOf()
        ctx1[Identifier(MODID, "entity")] = le
        return redirectMap[redirectId(id)]?.let { runRedirect(it, ctx1) } ?: id
    }

    private fun redirectId(id: Identifier): Identifier =
            Identifier(id.namespace, id.path
                .split(".").dropLast(1).joinToString("")
                .let { if (it == "") id.path else it }
                .removePrefix("varied/").let { "varied/$it.json" }
            )


    private fun runRedirect(ob: VariedChooser, ctx: Ctx): Identifier? {
        return ob.choose(ctx)
    }

    private fun parseVaried(resource: Resource) = parseChooser(resource.inputStream)

    override fun prepare(manager: ResourceManager, profiler: Profiler): Map<Identifier, VariedChooser> = profiler.profiling {
        manager.allNamespaces.toList().flatMap { namespace ->
            manager.findResources(Identifier(namespace, "varied")) { true }
        }.map {
            it to parseVaried(manager.getResource(it))
        }.toMap()
    }

    override fun apply(loader: Map<Identifier, VariedChooser>, manager: ResourceManager, profiler: Profiler) = profiler.profiling {
        redirectMap = loader
    }
}
