package com.localmesh.client

class WebSocketClient {
    
    private var isConnected = false
    private var serverUrl = ""
    
    fun connect(serverIp: String) {
        this.serverUrl = "ws://$serverIp:8887"
        this.isConnected = true
        println("Подключение к серверу: $serverUrl")
    }
    
    fun sendMessage(text: String, username: String) {
        if (!isConnected) {
            println("Нет подключения к серверу")
            return
        }
        
        println("Отправка сообщения: $text от $username")
    }
    
    fun sendSOS(latitude: Double, longitude: Double) {
        println("Отправка SOS сигнала с координатами: $latitude, $longitude")
    }
    
    fun disconnect() {
        isConnected = false
        println("Отключение от сервера")
    }
}
