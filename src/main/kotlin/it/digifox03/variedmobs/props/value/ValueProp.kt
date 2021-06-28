package it.digifox03.variedmobs.props.value

import it.digifox03.variedmobs.props.GenericProp

interface ValueProp: GenericProp<Float> {
    val min: Float
        get() = Float.MIN_VALUE
    val max: Float
        get() = Float.MAX_VALUE
    val minIsInclusive: Boolean
        get() = true
    val maxIsInclusive: Boolean
        get() = true
}
