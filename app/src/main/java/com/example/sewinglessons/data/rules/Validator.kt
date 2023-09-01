package com.example.sewinglessons.data.rules

object Validator {
    private val EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    fun validateFirstName(fName: String): ValidationResult {
        return ValidationResult(
            (!fName.isNullOrEmpty() && fName.length >= 2)
        )

    }

    fun validateLastName(lName: String): ValidationResult {
        return ValidationResult(
            (!lName.isNullOrEmpty() && lName.length >= 2)
        )
    }

    fun validateEmail(email: String): ValidationResult {
        val isValidEmail = email.matches(EMAIL_REGEX.toRegex())
        return ValidationResult(isValidEmail)
    }

    fun validatePassword(password: String): ValidationResult {
        val isPasswordValid = password.length >= 6
        return ValidationResult(isPasswordValid)
    }
}

data class ValidationResult (
    val status: Boolean = false
)