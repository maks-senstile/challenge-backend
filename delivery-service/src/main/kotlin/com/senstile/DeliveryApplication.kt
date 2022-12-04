package com.senstile

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class DeliveryApplication {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<DeliveryApplication>(*args)
        }
    }
}
