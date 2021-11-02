package com.plusmobileapps

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.plusmobileapps.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
