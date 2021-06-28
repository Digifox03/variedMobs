package it.digifox03.variedmobs

import it.digifox03.variedmobs.selectors.Selector
import kotlinx.serialization.Serializable

@Serializable
internal class SelectorRoot(
    val root: Selector,
    val version: Int
)
