package com.topaz.easymessenger.data

data class ChatMessage(
    val id: String,
    val message: String,
    val fromId: String,
    val toId: String,
    val timestamp: Long
) {
    constructor() : this("", "", "", "", -1)
}