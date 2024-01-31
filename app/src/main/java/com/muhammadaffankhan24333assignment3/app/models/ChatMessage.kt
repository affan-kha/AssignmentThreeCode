package com.muhammadaffankhan24333assignment3.app.models

import java.io.Serializable

data class ChatMessage(
    val senderId: String,
    val message: String,
    val timestamp: Long
):Serializable{
    constructor():this("","",0)
}