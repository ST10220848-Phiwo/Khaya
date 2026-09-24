package com.example.khaya.domain.model


/**[EXTERNAL]
 * "ViewModel + StateFlow + sealed UI state" pattern
 * Source: Android Developer, "UI layer / State holders" traced to Life360s supposed stack
 * offline-first cache is serving the loading purpose during loadshedding
 */

 sealed interface UIState <out T> {
     data object Loading : UIState<Nothing>
     data class Success<T>(val data: T, val fromCache: Boolean = false) : UIState<T>
    data class Error(val message: String, val cause: Throwable? = null) : UIState<Nothing>
 }

/** Auth gets it own states because navigation depends on them */
sealed interface AuthState {
    data object Unknown :  AuthState
    data object Authenticated : AuthState
    data object Unauthenticated : AuthState
    data class SignedIn(val user: User, val isNewUser: Boolean) : AuthState
    data class Failed(val reason: String) : AuthState
}


