package com.example.effectivemobile

import androidx.lifecycle.ViewModel
import com.example.effectivemobile.domain.usecase.LoginByEmailUseCase
import com.example.effectivemobile.domain.usecase.LoginResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val isButtonEnabled: Boolean = false,
    val error: String? = null
)

class LoginViewModel(
    private val loginByEmailUseCase: LoginByEmailUseCase
) : ViewModel() {


    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _events = Channel<LoginEvent>(Channel.BUFFERED)
    val events: Flow<LoginEvent> = _events.receiveAsFlow()

    fun onEmailChanged(v: String) {
        val email = v.trim()
        _state.update {
            it.copy(
                email = email,
                isButtonEnabled = email.contains("@") && email.contains("."),
                error = null
            )
        }
    }

    fun onPassChanged(v: String) {
        _state.update { it.copy(pass = v, error = null) }
    }

    fun onLoginClick() {
        val s = _state.value
        when (loginByEmailUseCase.execute(s.email, s.pass)) {
            is LoginResult.Success -> {
                _state.update { it.copy(error = null) }
                _events.trySend(LoginEvent.Success)
            }
            is LoginResult.Error -> {
                _state.update { it.copy(error = "Введите e-mail вида имя@домен.зона") }
            }
        }
    }
}

sealed class LoginEvent {
    object Success : LoginEvent()
}
