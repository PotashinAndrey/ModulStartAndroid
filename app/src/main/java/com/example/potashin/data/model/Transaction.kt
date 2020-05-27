package com.example.potashin.data.model

import java.sql.Timestamp
import java.util.*

data class Transaction (
    val comment: String,
    val email: String,
    val pattern: Boolean,

    val recipient: UUID,
    val recipientNumber: String,

    val sender: UUID,
    val senderNumber: String,

    val sum: Number,

    val timestamp: String, // Timestamp
    val transactionName: String
) {
    fun toStringInList() : String {
        return "${transactionName} ${sum} ${timestamp}"
    }
}
