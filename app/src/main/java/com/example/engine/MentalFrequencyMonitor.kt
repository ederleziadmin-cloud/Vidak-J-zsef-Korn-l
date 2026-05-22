package com.example.engine

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class MentalFrequencyMonitor {
    // Szimulált frekvencia-adatok a rendszer állapotából (pl. latency/cpu)
    val frequencyFlow = flow {
        while(true) {
            // MHz/Hz alapú frekvenciaszinkron
            emit(Random.nextDouble(40.0, 60.0))
            delay(1000)
        }
    }
}
