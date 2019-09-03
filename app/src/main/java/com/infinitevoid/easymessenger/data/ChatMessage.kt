package com.infinitevoid.easymessenger.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatMessage(
    val id: String,
    val message: String,
    val fromId: String,
    val toId: String,
    val timestamp: Long,
    val imageURL: String,
    var read: String
) : Parcelable {
    constructor() : this("", "", "", "", -1, "", "false")
}