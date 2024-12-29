package com.example.common.utils.functions

import kotlin.random.Random

fun randomInteger(min: Int, max: Int): Int {
    return Random.nextInt(min, max)
}

fun randomDouble(min: Double = 0.0, max: Double): Double {
    return Random.nextDouble(min, max)
}