package com.muhammadaffankhan24333assignment3.app.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.muhammadaffankhan24333assignment3.app.databinding.ActivitySignUpBinding
import com.muhammadaffankhan24333assignment3.app.models.User
import com.muhammadaffankhan24333assignment3.app.utils.ValidationUtil
import com.muhammadaffankhan24333assignment3.app.viewmodel.AuthViewModel


class SignUpActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var context: Context

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this


        binding.signInView.setOnClickListener {
            Intent(context, SignInActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(this)
            }.apply {
                finish()
            }
        }

        binding.signUpBtn.setOnClickListener {
            if (validateForm()) {
                // Form is valid, proceed with signup logic
                val name = binding.signUpName.text.toString()
                val username = binding.signUpUserName.text.toString()
                val email = binding.signUpEmail.text.toString()
                val password = binding.signUpPassword.text.toString()
                      startLoading(context)
                    authViewModel.signUp(email, password) { success, errorMessage ->
                       dismiss()
                        if (success) {
                            // Sign-in successful
                            // Navigate to the next screen or perform any other action

                            if (authViewModel.checkUserAuth()) {
                                val user = User(
                                    authViewModel.getLoggedUserId(),
                                    name,
                                    username,
                                    email,
                                    System.currentTimeMillis(),
                                    System.currentTimeMillis()
                                )


                                authViewModel.saveUserData(user)

                                Intent(context, SignInActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(this)
                                }.apply {
                                    finish()
                                }
                            }

                        } else {
                            // Sign-in failed, show error message
                            Toast.makeText(this@SignUpActivity, errorMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
        }
    }

    private fun validateForm(): Boolean {
        val name = binding.signUpName.text.toString()
        val username = binding.signUpUserName.text.toString()
        val email = binding.signUpEmail.text.toString()
        val password = binding.signUpPassword.text.toString()
        val confirmPassword = binding.signUpConfirmPassword.text.toString()

        if (ValidationUtil.isNullOrEmpty(name)) {
            binding.signUpName.error = "Full Name cannot be empty"
            return false
        }

        if (ValidationUtil.isNullOrEmpty(username)) {
            binding.signUpUserName.error = "Username cannot be empty"
            return false
        }

        if (ValidationUtil.isNullOrEmpty(email)) {
            binding.signUpEmail.error = "Email cannot be empty"
            return false
        }

        if (!ValidationUtil.isValidEmail(email)) {
            binding.signUpEmail.error = "Invalid email address"
            return false
        }

        if (ValidationUtil.isNullOrEmpty(password)) {
            binding.signUpPassword.error = "Password cannot be empty"
            return false
        }

        if (!ValidationUtil.isPasswordValid(password)) {
            binding.signUpPassword.error = "Password must be at least 6 characters long"
            return false
        }

        if (!ValidationUtil.doPasswordsMatch(password, confirmPassword)) {
            binding.signUpConfirmPassword.error = "Passwords do not match"
            return false
        }

        return true
    }
}