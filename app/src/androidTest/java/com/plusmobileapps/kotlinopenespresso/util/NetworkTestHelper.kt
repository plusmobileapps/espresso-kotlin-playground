package com.plusmobileapps.kotlinopenespresso.util

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.http.*

fun mockHttpClient(handler: MockRequestHandler): HttpClient {
    return HttpClient(MockEngine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        engine {
            addHandler(handler)
        }
    }
}

val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"