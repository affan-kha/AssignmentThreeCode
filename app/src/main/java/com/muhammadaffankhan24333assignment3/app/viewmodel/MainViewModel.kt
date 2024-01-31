package com.muhammadaffankhan24333assignment3.app.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.muhammadaffankhan24333assignment3.app.models.ChatMessage
import com.muhammadaffankhan24333assignment3.app.models.User
import com.muhammadaffankhan24333assignment3.app.ui.activities.BaseActivity


class MainViewModel : ViewModel() {
    private val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
    private val chatsRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Chats")
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    fun logout() {
        auth.signOut()
    }

    fun getAllUsers(loggedUserId:String,callback: (Boolean, List<User>?) -> Unit) {
        val list = mutableListOf<User>()
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val user = userSnapshot.getValue(User::class.java)
                        if (user != null && user.id != loggedUserId) {
                            list.add(user)
                        }
                    }
                    callback(true,list)
                } else {
                    // Handle the case when no users are found
                    callback(true,list)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors
                callback(false,list)
            }
        })
    }

    fun saveChatMessage(chatId:String,senderId:String,message:String,timeStamp:Long){
        val messageId = chatsRef.child(chatId).child("messages").push().key

        val messageData = mapOf(
            "senderId" to senderId,
            "message" to message,
            "timestamp" to timeStamp
        )

        if (messageId != null) {
            chatsRef.child(chatId).child("messages").child(messageId).setValue(messageData)
        }
    }

    fun getChatHistory(chatId: String,callback: (Boolean, List<ChatMessage>) -> Unit){
        val messages: MutableList<ChatMessage> = mutableListOf()
        chatsRef.child(chatId).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Retrieve and process messages from dataSnapshot
                    if(dataSnapshot.exists()){
                        for (messageSnapshot in dataSnapshot.children) {
                            val message = messageSnapshot.getValue(ChatMessage::class.java)
                            if (message != null) {
                                messages.add(message)
                            }
                        }
                        callback(true,messages)
                    }
                    else{
                        callback(true,messages)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                    callback(false,messages)
                }
            })
    }

    fun fetchUserDetails(id:String){
        usersRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data found
                    val fetchUser = dataSnapshot.getValue(User::class.java)
                    BaseActivity.loggedUser = fetchUser
                } else {
                    // User not found
                    Log.d("Firebase", "User not found")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error fetching user data", databaseError.toException())
            }
        })
    }
}