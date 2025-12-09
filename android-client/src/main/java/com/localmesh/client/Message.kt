package com.localmesh.client

data class Message(
    val text: String,
    val sender: String,
    val timestamp: Long = System.currentTimeMillis(),
    val type: String = "TEXT",
    val latitude: Double? = null,
    val longitude: Double? = null
) {
    fun isSOS(): Boolean = type == "SOS"
    fun hasLocation(): Boolean = latitude != null && longitude != null
}
