package com.muhammadaffankhan24333assignment3.app.utils

class ValidationUtil {
    companion object {
        fun isNullOrEmpty(value: String?): Boolean {
            return value.isNullOrBlank()
        }

        fun isValidEmail(email: String): Boolean {
            val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
            return email.matches(emailRegex.toRegex())
        }

        fun isPasswordValid(password: String): Boolean {
            // Customize your password validation logic here
            return password.length >= 6
        }

        fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
            return password == confirmPassword
        }
    }
}