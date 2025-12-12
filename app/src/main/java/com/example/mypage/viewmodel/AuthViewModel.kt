package com.example.mypage.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.CoroutineContext

// Data class representing Firebase user
data class FirebaseUserData(
    val uid: String = "",
    val username: String = "",
    val email: String = ""
)

class AuthViewModel : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()


    fun signUp(username: String, email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = FirebaseUserData(uid = userId, username = username, email = email)

                    firestore.collection("users")
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            _authState.value = AuthState.Success("Account created successfully!")
                        }
                        .addOnFailureListener { e ->
                            auth.currentUser?.delete()
                            _authState.value = AuthState.Error("Failed to save user data: ${e.message}")
                        }
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthUserCollisionException -> "Email already in use"
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email format"
                        else -> task.exception?.message ?: "Sign up failed"
                    }
                    _authState.value = AuthState.Error(errorMessage)
                }
            }
    }


    fun signIn(email: String, password: String) {
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success("Login successful!")
                } else {
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "No account found with this email"
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email or password"
                        else -> task.exception?.message ?: "Login failed"
                    }
                    _authState.value = AuthState.Error(errorMessage)
                }
            }
    }


    fun signInWithGoogleClassic(
        context: Context,
        webClientId: String,
        launcher: Nothing?
    ) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        // DÃ©connectez
        googleSignInClient.signOut().addOnCompleteListener {
            launcher?.launch(googleSignInClient.signInIntent as CoroutineContext)
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val result = auth.signInWithCredential(credential).await()
                val firebaseUser = result.user
                if (firebaseUser != null) {
                    val userRef = firestore.collection("users").document(firebaseUser.uid)
                    userRef.get().addOnSuccessListener { doc ->
                        if (!doc.exists()) {
                            val user = FirebaseUserData(
                                uid = firebaseUser.uid,
                                username = firebaseUser.displayName ?: "",
                                email = firebaseUser.email ?: ""
                            )
                            userRef.set(user)
                        }
                    }
                    _authState.value = AuthState.Success("Signed in with Google successfully!")
                } else {
                    _authState.value = AuthState.Error("Google Sign-In failed")
                }
            } catch (e: ApiException) {
                _authState.value = AuthState.Error("Google Sign-In failed: ${e.message}")
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Google Sign-In failed: ${e.message}")
            }
        }
    }


    fun resetAuthState() {
        _authState.value = AuthState.Idle
    }


    fun signOut() {
        auth.signOut()
        _authState.value = AuthState.Idle
    }

    fun isUserLoggedIn(): Boolean = auth.currentUser != null
}

private fun Nothing?.launch(context: kotlin.coroutines.CoroutineContext) {}
