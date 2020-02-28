package dano.kotlin.kontlinrest.adapters.rest

import java.time.Instant

data class ErrorDetail(
        val type: String,
        val title: String,
        val status: Int,
        val detail: String,
        val instance: String,
        val timestamp: Instant,
        val requestPath: String
)
