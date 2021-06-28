package it.digifox03.variedmobs

import net.minecraft.util.Identifier

object RedirectContext {
    @JvmField val entity = Identifier(VariedMobs.modId, "entity")
    @JvmField val slot = Identifier(VariedMobs.modId, "slot")
    @JvmField val item = Identifier(VariedMobs.modId, "item")
}
