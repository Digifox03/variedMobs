package it.digifox03.variedmobs.selectors

import kotlin.random.Random

fun <T> MutableList<Pair<T, Double>>.randomWPop(random: Random): T? {
    var choice = random.nextDouble(.0, fold(.0) {a, (_, b) -> a + b})
    val res = indexOfFirst { (_, b) -> choice -= b; choice <= 0 }
    return getOrNull(res)?.first.also { removeAt(res) }
}
