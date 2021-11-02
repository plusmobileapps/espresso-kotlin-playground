package com.plusmobileapps.kotlinopenespresso.data.model
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)