package com.example.data.remote.thirdpartyregister

import android.content.Context
import android.util.Log
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object GoogleUtils {

    fun getGoogleUserData(
        credentialManager: androidx.credentials.CredentialManager,
        clientId :String,
        lifecycleOwner: LifecycleOwner,
        context : Context
    ){
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(clientId)
            .setAutoSelectEnabled(false)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        lifecycleOwner.lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(context, request)
                val token = GoogleIdTokenCredential.createFrom(result.credential.data)
                val firebaseCredential = GoogleAuthProvider.getCredential(token.idToken,null)
                val a = Firebase.auth.signInWithCredential(firebaseCredential).await()
                Log.e("uid",a.user?.uid.toString())
                Log.e("tenantid",a.user?.tenantId.toString())
                Log.e("providerid",a.user?.providerId.toString())
                Log.e("idid",a.credential?.provider.toString())
                Log.e("ididid",a.additionalUserInfo?.profile?.keys.toString())



            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}