package com.plusmobileapps.kotlinopenespresso.util

import com.plusmobileapps.kotlinopenespresso.data.LoginDataSource
import com.plusmobileapps.kotlinopenespresso.data.model.LoginResponse
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MockNetworkTestHelper {

    val client = HttpClient(MockEngine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
        engine {
            addHandler { request: HttpRequestData ->
                when (request.url.fullUrl) {
                    LoginDataSource.LOGIN_URL -> getLoginResponse.invoke(this, request)
                    else -> error("Unhandled ${request.url.fullUrl}")
                }
            }
        }
    }

    private var getLoginResponse: MockRequestHandler = defaultLoginResponseHandler

    fun everyLoginReturns(response: MockRequestHandler) {
        this.getLoginResponse = response
    }

    fun destroy() {
        getLoginResponse = defaultLoginResponseHandler
    }

    companion object {
        val defaultLoginResponseHandler: MockRequestHandler = {
            respond(
                Json.encodeToString(LoginResponse("default-id", "Buzz Killington")),
                HttpStatusCode.OK,
                headersOf("Content-Type", ContentType.Application.Json.toString())
            )
        }
    }
}

val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"