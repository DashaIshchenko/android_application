package com.localmesh.client

data class User(
    val id: String,
    val name: String,
    val isOnline: Boolean = true,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val lastSeen: Long = System.currentTimeMillis()
)
