package com.plusmobileapps.kotlinopenespresso.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val username: String, val password: String)

@Serializable
data class LoginResponse(val id: String, val displayName: String)

@Serializable
data class LoginError(val errorCode: Int, val message: String)
