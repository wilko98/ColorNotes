package com.infinit.colornotes.model

import androidx.annotation.Keep

@Keep
data class LoginResponse(
    val exists: Boolean?,
    val token: String?
)