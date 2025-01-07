package com.example.eventify.test

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.math.truncate


suspend fun test1(){
    delay(3000)
}

suspend fun test2() :Boolean {
    return withContext(Dispatchers.IO) {
        delay(5000)
        true
    }
}


fun main() {

    val job = CoroutineScope(Dispatchers.IO).launch {
        test1()
        println("ALGJLASGJKLASGK")
        test2()
    }
    runBlocking {
        job.join()
    }

}

