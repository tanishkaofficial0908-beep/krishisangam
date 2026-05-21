package com.example.krishisangam.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

enum class UserRole {
    BUYER,
    FARMER,
    NODE_MANAGER,
    TRANSPORTATION
}

data class AuthUiState(
    val currentUser: FirebaseUser? = null,
    val selectedRole: UserRole? = null,
    val isLoading: Boolean = false,
    val message: String? = null,
    val isError: Boolean = false
)

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    var uiState by mutableStateOf(
        AuthUiState(currentUser = auth.currentUser)
    )
        private set

    fun selectRole(role: UserRole) {
        uiState = uiState.copy(
            selectedRole = role,
            message = null,
            isError = false
        )
    }

    fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: () -> Unit
    ) {
        val trimmedFirstName = firstName.trim()
        val trimmedLastName = lastName.trim()
        val trimmedEmail = email.trim()

        if (
            trimmedFirstName.isBlank() ||
            trimmedLastName.isBlank() ||
            trimmedEmail.isBlank() ||
            password.isBlank() ||
            confirmPassword.isBlank()
        ) {
            showError("Please fill all fields")
            return
        }

        if (password.length < 6) {
            showError("Password must be at least 6 characters")
            return
        }

        if (password != confirmPassword) {
            showError("Passwords do not match")
            return
        }

        val selectedRole = uiState.selectedRole ?: UserRole.BUYER

        uiState = uiState.copy(
            isLoading = true,
            message = null,
            isError = false
        )

        auth.createUserWithEmailAndPassword(trimmedEmail, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    if (user == null) {
                        showError("Account created but user profile was not found.")
                        return@addOnCompleteListener
                    }

                    val fullName = "$trimmedFirstName $trimmedLastName"

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()

                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                saveUserProfileToFirestore(
                                    user = user,
                                    firstName = trimmedFirstName,
                                    lastName = trimmedLastName,
                                    email = trimmedEmail,
                                    role = selectedRole,
                                    onSuccess = {
                                        user.sendEmailVerification()
                                            .addOnCompleteListener {
                                                uiState = uiState.copy(
                                                    isLoading = false,
                                                    currentUser = auth.currentUser,
                                                    message = "Account created. Verification email sent.",
                                                    isError = false
                                                )
                                                onSuccess()
                                            }
                                    },
                                    onFailure = { errorMessage ->
                                        showError(errorMessage)
                                    }
                                )
                            } else {
                                showError(
                                    profileTask.exception?.message
                                        ?: "Account created but failed to save profile name."
                                )
                            }
                        }
                } else {
                    showError(getReadableAuthError(task.exception))
                }
            }
    }

    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        if (email.isBlank() || password.isBlank()) {
            showError("Please enter email and password")
            return
        }

        uiState = uiState.copy(
            isLoading = true,
            message = null,
            isError = false
        )

        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    if (user?.isEmailVerified == true) {
                        uiState = uiState.copy(
                            isLoading = false,
                            currentUser = user,
                            message = "Login successful",
                            isError = false
                        )
                        onSuccess()
                    } else {
                        user?.sendEmailVerification()
                        showError("Please verify your email first. Verification link sent again.")
                    }
                } else {
                    showError(getReadableAuthError(task.exception))
                }
            }
    }

    fun resetPassword(email: String) {
        if (email.isBlank()) {
            showError("Please enter your email")
            return
        }

        uiState = uiState.copy(
            isLoading = true,
            message = null,
            isError = false
        )

        auth.sendPasswordResetEmail(email.trim())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uiState = uiState.copy(
                        isLoading = false,
                        message = "Password reset email sent",
                        isError = false
                    )
                } else {
                    showError(getReadableAuthError(task.exception))
                }
            }
    }

    fun resendVerificationEmail() {
        val user = auth.currentUser

        if (user == null) {
            showError("No user found. Please login again.")
            return
        }

        uiState = uiState.copy(
            isLoading = true,
            message = null,
            isError = false
        )

        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uiState = uiState.copy(
                        isLoading = false,
                        message = "Verification email sent again",
                        isError = false
                    )
                } else {
                    showError(task.exception?.message ?: "Failed to send verification email")
                }
            }
    }

    fun signOut() {
        auth.signOut()
        uiState = AuthUiState()
    }

    private fun saveUserProfileToFirestore(
        user: FirebaseUser,
        firstName: String,
        lastName: String,
        email: String,
        role: UserRole,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val uid = user.uid
        val fullName = "$firstName $lastName"

        val userProfile = hashMapOf(
            "uid" to uid,
            "firstName" to firstName,
            "lastName" to lastName,
            "fullName" to fullName,
            "email" to email,
            "role" to role.name.lowercase(),
            "phone" to "",
            "createdAt" to FieldValue.serverTimestamp(),
            "updatedAt" to FieldValue.serverTimestamp()
        )

        firestore.collection("users")
            .document(uid)
            .set(userProfile)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(
                    exception.message ?: "Account created but failed to save user profile."
                )
            }
    }

    private fun showError(message: String) {
        uiState = uiState.copy(
            isLoading = false,
            message = message,
            isError = true
        )
    }

    private fun getReadableAuthError(exception: Exception?): String {
        return when (exception) {
            is FirebaseAuthWeakPasswordException -> {
                "Password is too weak. Use at least 6 characters."
            }

            is FirebaseAuthInvalidCredentialsException -> {
                "Wrong email or password. Please check and try again."
            }

            is FirebaseAuthInvalidUserException -> {
                "This account does not exist. Please create an account first."
            }

            is FirebaseAuthUserCollisionException -> {
                "This email is already registered. Please sign in instead."
            }

            is FirebaseNetworkException -> {
                "Network error. Please check your internet connection."
            }

            else -> {
                exception?.message ?: "Something went wrong. Please try again."
            }
        }
    }
}