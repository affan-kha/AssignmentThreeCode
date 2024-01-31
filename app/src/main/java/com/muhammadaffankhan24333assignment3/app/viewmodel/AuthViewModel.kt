package com.muhammadaffankhan24333assignment3.app.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.muhammadaffankhan24333assignment3.app.models.User


class AuthViewModel : ViewModel() {
    private val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Users")
    private val chatsRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Chats")
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
     fun signUp(email: String, password: String,callback: (Boolean, String?) -> Unit) {
         auth.createUserWithEmailAndPassword(email, password)
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     // User signed up successfully
                     callback(true, null)
                 } else {
                     // Sign up failed
                     callback(false, task.exception?.message)
                 }
             }
    }

     fun signIn(email: String, password: String,callback: (Boolean, String?) -> Unit) {
         auth.signInWithEmailAndPassword(email, password)
             .addOnCompleteListener { task ->
                 if (task.isSuccessful) {
                     // User signed in successfully
                     callback(true, null)
                 } else {
                     // Sign in failed
                     callback(false, task.exception?.message)
                 }
             }
    }

     fun saveUserData(user: User) {
         val userId = auth.currentUser?.uid
         userId?.let {
             usersRef.child(it).setValue(user)
         }
    }

    fun checkUserAuth():Boolean{
        return auth.currentUser != null
    }

    fun getLoggedUserId():String{
        return auth.currentUser!!.uid
    }
}