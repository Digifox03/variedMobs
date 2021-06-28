package it.digifox03.variedmobs.props

import net.minecraft.util.Identifier

interface GenericProp<T> {
    fun read(context: Map<Identifier, Any>): T
}
