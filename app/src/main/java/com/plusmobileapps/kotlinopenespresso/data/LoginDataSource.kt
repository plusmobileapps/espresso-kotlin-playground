package com.plusmobileapps.kotlinopenespresso.data

import com.plusmobileapps.kotlinopenespresso.OpenForTest
import com.plusmobileapps.kotlinopenespresso.data.model.LoggedInUser
import com.plusmobileapps.kotlinopenespresso.data.model.LoginError
import com.plusmobileapps.kotlinopenespresso.data.model.LoginRequest
import com.plusmobileapps.kotlinopenespresso.data.model.LoginResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
@OpenForTest
class LoginDataSource @Inject constructor(private val httpClient: HttpClient) {

    companion object {
        const val LOGIN_URL = "https://plusmobileapps.com/login"
    }

    suspend fun login(email: String, password: String): Result<LoggedInUser> = withContext(Dispatchers.IO) {
        try {
            val request = httpClient.post(LOGIN_URL) {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email, password))
            }
            if (request.status.isSuccess()) {
                val response = request.body<LoginResponse>()
                val user = LoggedInUser(response.id, response.displayName)
                Result.Success(user)
            } else {
                val response = request.bodyAsText()
                val error = Json.decodeFromString<LoginError>(response)

                Result.Error(IOException(error.message))
            }
        } catch (e: Throwable) {
            Result.Error(IOException(e.message))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}