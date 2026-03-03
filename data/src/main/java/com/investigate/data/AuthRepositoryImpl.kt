package com.investigate.data

import android.app.Activity
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.investigate.domain.model.User
import com.investigate.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val clientId: String,
    private val firebaseAuth: FirebaseAuth
): AuthRepository {

    override suspend fun signInWithGoogle(activityContext: Context): Result<User?> {
        val credentialManager = CredentialManager.create(activityContext)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(clientId)
            .setFilterByAuthorizedAccounts(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            context = activityContext,
            request = request
        )

        val credential = result.credential

        return if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleCredential =
                GoogleIdTokenCredential.createFrom(credential.data)

            val firebaseCredential = GoogleAuthProvider.getCredential(
                googleCredential.idToken,
                null
            )

            val authResult = firebaseAuth.signInWithCredential(
                firebaseCredential
            ).await()

            getUser(authResult.user)?.let {
                Result.success(it)
            } ?: Result.failure(Exception("User is null"))
        } else {
            Result.failure(Exception("Invalid credential"))
        }
    }

    override suspend fun signOut(activityContext: Context) {
        val credentialManager = CredentialManager.create(activityContext)

        firebaseAuth.signOut()
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
    }

    override fun getUser(): User? {
        return getUser(firebaseAuth.currentUser)
    }

    private fun getUser(firebaseUser: FirebaseUser?): User? {
        val email = firebaseUser?.email
        val uid = firebaseUser?.uid
        return if (email != null && uid != null) User(email, uid)
            else null
    }

}