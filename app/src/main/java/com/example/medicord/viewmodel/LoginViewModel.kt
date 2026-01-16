package com.example.medicord.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medicord.Data.database.AppDatabase
import com.example.medicord.Data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false,
    val loginAttempts: Int = 0
)

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(username = username, errorMessage = null)
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, errorMessage = null)
    }

    fun login() {
        val currentState = _uiState.value
        if (currentState.username.isBlank() || currentState.password.isBlank()) {
            _uiState.value = currentState.copy(errorMessage = "Username dan password harus diisi")
            return
        }

        _uiState.value = currentState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val user = userRepository.login(currentState.username, currentState.password)
                if (user != null) {
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        isLoginSuccess = true,
                        errorMessage = null,
                        loginAttempts = 0
                    )
                } else {
                    val attempts = currentState.loginAttempts + 1
                    if (attempts >= 3) {
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            errorMessage = "Login gagal 3 kali. Aplikasi terkunci.",
                            loginAttempts = attempts
                        )
                    } else {
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            errorMessage = "Username atau password salah",
                            loginAttempts = attempts
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    errorMessage = "Terjadi kesalahan: ${e.message}"
                )
            }
        }
    }

    fun resetLoginState() {
        _uiState.value = LoginUiState()
    }
}
