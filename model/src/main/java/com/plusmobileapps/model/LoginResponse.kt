package com.plusmobileapps.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(val id: String, val displayName: String)