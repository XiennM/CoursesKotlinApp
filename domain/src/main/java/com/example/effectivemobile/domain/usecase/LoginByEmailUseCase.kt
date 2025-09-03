package com.example.effectivemobile.domain.usecase

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class LoginByEmailUseCase {

    private val emailRegex = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")

    fun execute(email: String, password: String) : LoginResult {
        return if (emailRegex.matches(email)) LoginResult.Success
        else LoginResult.Error("Неверный формат e-mail")
    }

}