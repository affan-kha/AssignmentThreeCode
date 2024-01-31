package com.muhammadaffankhan24333assignment3.app.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.muhammadaffankhan24333assignment3.app.databinding.ActivitySignInBinding
import com.muhammadaffankhan24333assignment3.app.utils.ValidationUtil
import com.muhammadaffankhan24333assignment3.app.viewmodel.AuthViewModel


class SignInActivity : BaseActivity() {

    private lateinit var binding: ActivitySignInBinding

    private lateinit var context: Context

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this

        if(authViewModel.checkUserAuth()){
            loggedUserId = authViewModel.getLoggedUserId()
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
            }.apply {
                finish()
            }
        }

        binding.signUpView.setOnClickListener {
            Intent(context, SignUpActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
            }.apply {
                finish()
            }
        }

        binding.signInBtn.setOnClickListener {
            if (validateForm()) {
                val email = binding.signInEmailBox.text.toString()
                val password = binding.signInPasswordBox.text.toString()
                   startLoading(context)
                  authViewModel.signIn(email, password) { success, errorMessage ->
                      dismiss()
                      if (success) {
                          // Sign-in successful
                          // Navigate to the next screen or perform any other action
                          loggedUserId = authViewModel.getLoggedUserId()
                          Intent(context, MainActivity::class.java).apply {
                              flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                              startActivity(this)
                          }.apply {
                              finish()
                          }
                      } else {
                          // Sign-in failed, show error message
                          Toast.makeText(this@SignInActivity, errorMessage, Toast.LENGTH_SHORT).show()
                      }
                  }

            }
        }
    }

    private fun validateForm(): Boolean {

        val email = binding.signInEmailBox.text.toString()
        val password = binding.signInPasswordBox.text.toString()


        if (ValidationUtil.isNullOrEmpty(email)) {
            binding.signInEmailBox.error = "Email cannot be empty"
            return false
        }

        if (!ValidationUtil.isValidEmail(email)) {
            binding.signInEmailBox.error = "Invalid email address"
            return false
        }

        if (ValidationUtil.isNullOrEmpty(password)) {
            binding.signInPasswordBox.error = "Password cannot be empty"
            return false
        }

        if (!ValidationUtil.isPasswordValid(password)) {
            binding.signInPasswordBox.error = "Password must be at least 6 characters long"
            return false
        }

        return true
    }
}