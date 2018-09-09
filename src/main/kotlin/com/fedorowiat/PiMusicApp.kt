package com.fedorowiat

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableAsync
@EnableScheduling
open class PiMusicApp

fun main(args: Array<String>) {
    SpringApplication(PiMusicApp::class.java).run()
}