package com.example.eventify.test


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


suspend fun test1(){
    delay(3000)
}

suspend fun test2()  {
     withContext(Dispatchers.IO) {
        delay(5000)
         println("ASKLG")

    }
}



fun main() {
    val mutable = mutableListOf(1,2,3,4,5,6)
    mutable.removeAll { true }
    println(mutable)


}

