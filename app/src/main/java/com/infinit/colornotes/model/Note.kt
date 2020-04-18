package com.infinit.colornotes.model

import androidx.annotation.Keep
import androidx.room.Entity

@Keep
@Entity
data class Note(
    val Id: Int?,
    val color: String?,
    val date: String?,
    val done: Int?,
    val text: String?,
    val title: String?
)