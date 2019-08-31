package com.topaz.easymessenger.data

data class ChatMessage(
    val id: String,
    val message: String,
    val fromId: String,
    val toId: String,
    val timestamp: Long,
    var read: String
) {
    constructor() : this("", "", "", "", -1, "false")
}