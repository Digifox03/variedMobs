package it.digifox03.variedmobs

import kotlinx.serialization.modules.SerializersModule

interface VariedMobsSelectorRegister {
    fun module(): SerializersModule
}
