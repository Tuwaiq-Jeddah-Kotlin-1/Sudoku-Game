package com.renad.sudoku.firebase

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.renad.sudoku.R

class Auth() {
    private var firebaseAuth = Firebase.auth
    private lateinit var googleSignInClient: GoogleSignInClient

    //    val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
    companion object {
        private const val RC_SIGN_IN = 1998
    }

    //Anonymous SignIn
    fun anonymousSignIn(context: Context) {
        val sharedPref: SharedPreferences =
            context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        firebaseAuth.signInAnonymously()
            .addOnSuccessListener {
                sharedPref.edit().putBoolean("SignedIN", true).apply()
            }
            .addOnFailureListener {
                Log.d("AuthError", "anonymousAuth :$it")
            }
    }

    //Google SignIn
    //check signed in
    fun checkSignedIn(): Boolean {
        val user = Firebase.auth.currentUser
        return user != null
    }

    fun getUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

    //Configure Google Sign In
    fun configureGoogleSignIn(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun signIn(activity: Activity) {
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun checkGoogleSignIn(requestCode: Int, activity: Activity,context: Context,data: Intent?) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!,activity, context )
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                }
            } else {
                Log.w("signInActivity", exception.toString())

            }
        }
    }

     fun firebaseAuthWithGoogle(idToken: String, activity: Activity,context: Context) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("signInActivity", "signInWithCredential:success")
//                    firebaseAuth.currentUser!!.linkWithCredential(credential)
//                        .addOnCompleteListener(activity) { task ->
//                            if (task.isSuccessful) {
//                                Log.d(TAG, "linkWithCredential:success")
//                            } else {
//                                Log.w(TAG, "linkWithCredential:failure", task.exception)
//                                Toast.makeText(
//                                    activity, "Authentication failed.",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
                    Toast.makeText(context,"sign in success ",Toast.LENGTH_SHORT).show()
                }else {
             Toast.makeText(context,"sign in failure ${task.exception}",Toast.LENGTH_SHORT).show()
             Log.w("sign in fragment", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun signOut(){
        firebaseAuth.signOut()
    }

    fun getRequestCode():Int{
        return RC_SIGN_IN
    }
}