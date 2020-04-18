package com.infinit.colornotes.model

import androidx.annotation.Keep

@Keep
data class Credentials(
    val login: String?,
    val password: String?
)