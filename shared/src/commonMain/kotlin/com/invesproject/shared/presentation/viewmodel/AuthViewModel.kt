package com.invesproject.shared.presentation.viewmodel

import com.invesproject.shared.domain.model.User
import com.invesproject.shared.domain.model.UserRole
import com.invesproject.shared.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val _state = MutableStateFlow<AuthState>(AuthState.Initial)
    val state: StateFlow<AuthState> = _state.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    init {
        checkCurrentUser()
    }

    fun signUp(email: String, password: String, name: String, role: UserRole) {
        scope.launch {
            try {
                _state.value = AuthState.Loading
                val user = authRepository.signUp(email, password, name, role)
                _currentUser.value = user
                _state.value = AuthState.Success
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun signIn(email: String, password: String) {
        scope.launch {
            try {
                _state.value = AuthState.Loading
                val user = authRepository.signIn(email, password)
                _currentUser.value = user
                _state.value = AuthState.Success
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    fun signOut() {
        scope.launch {
            try {
                _state.value = AuthState.Loading
                authRepository.signOut()
                _currentUser.value = null
                _state.value = AuthState.Initial
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Sign out failed")
            }
        }
    }

    fun updateUserProfile(user: User) {
        scope.launch {
            try {
                _state.value = AuthState.Loading
                val updatedUser = authRepository.updateUserProfile(user)
                _currentUser.value = updatedUser
                _state.value = AuthState.Success
            } catch (e: Exception) {
                _state.value = AuthState.Error(e.message ?: "Failed to update profile")
            }
        }
    }

    private fun checkCurrentUser() {
        scope.launch {
            _currentUser.value = authRepository.getCurrentUser()
        }
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
} 