package com.muhammadaffankhan24333assignment3.app.models

import java.io.Serializable

data class User(
    val id: String = "",
    val name: String = "",
    val username: String = "",
    val email: String = "",
    val createdAt: Long = 0,
    val updatedAt: Long = 0
) : Serializable{
    constructor():this("","","","",0,0)
}