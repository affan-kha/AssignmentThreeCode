package com.muhammadaffankhan24333assignment3.app.ui.activities

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.muhammadaffankhan24333assignment3.app.R
import com.muhammadaffankhan24333assignment3.app.models.User

open class BaseActivity : AppCompatActivity() {

    companion object{
         var loggedUserId:String = ""
         var loggedUser: User? = null
        var alert: AlertDialog? = null

        fun generateChatId(user1Id: String, user2Id: String): String {
            val participants = listOf(user1Id, user2Id).sorted()
            return participants.joinToString("_")
        }

        fun startLoading(context: Context) {
            val builder = MaterialAlertDialogBuilder(context)
            val layout = LayoutInflater.from(context).inflate(R.layout.custom_loading, null)
            builder.setView(layout)
            builder.setCancelable(false)
            alert = builder.create()
            alert!!.show()
        }

        fun dismiss() {
            if (alert != null) {
                alert!!.dismiss()
            }
        }
    }

}