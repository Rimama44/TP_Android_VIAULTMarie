package com.example.tpviaultmarie.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class UserInfo(
    @SerialName("email")
    val email: String,
    @SerialName("firstname")
    val firstName: String,
    @SerialName("lastname")
    val lastName: String
)