package com.example.potashin.data.model

import java.util.*
import java.io.Serializable

data class User (
    val id: UUID,
    val email: String,
    val password: String,
    val name: String,
    val token: String,
    val passwordHash: String,
    val salt: String
) : Serializable {

}
