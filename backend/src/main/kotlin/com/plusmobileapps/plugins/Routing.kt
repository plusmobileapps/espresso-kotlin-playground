package com.plusmobileapps.plugins

import com.plusmobileapps.model.LoginRequest
import com.plusmobileapps.model.LoginResponse
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        post("/login") {
            val loginRequest: LoginRequest = call.receive()
            if (loginRequest.username == loginRequest.password) {
                call.respond(LoginResponse("some-id", "Chip Wellington"))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Username must equal the password")
            }
        }
    }
}