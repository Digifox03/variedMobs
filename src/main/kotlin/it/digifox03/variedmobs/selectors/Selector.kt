package it.digifox03.variedmobs.selectors

import net.minecraft.util.Identifier

interface Selector {
    fun select(context: Map<Identifier, Any>): Identifier
    val valid
        get() = true
}
