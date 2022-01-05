package com.renad.sudoku.ui.main_activity.fragments.settings_fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.renad.sudoku.R
import com.renad.sudoku.databinding.SettingsFragmentBinding
import com.renad.sudoku.ui.main_activity.MainActivity
import java.util.*


class SettingsFragment : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    private lateinit var viewModel: SettingsViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var binding: SettingsFragmentBinding

    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        val sharedPref =
            requireActivity().getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()


        binding.colorsModeToggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.darkBtn -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        editor.putBoolean("DARK_MOOD", true).apply()
                    }
                    R.id.lightBtn -> {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        editor.putBoolean("DARK_MOOD", false).apply()
                    }
                }
            }
        }



        binding.LanguageToggleButton.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.ArabicButton -> activity?.let {
                        editor.putString("LOCALE", "ar").apply()
                        setLocale(it, "ar")
                    }
                    R.id.EnglishButton -> activity?.let {
                        val editor = sharedPref.edit()
                        editor.putString("LOCALE", "en").apply()
                        setLocale(it, "en")
                    }
                }
            }
        }

//        viewModel.configureGoogleSign(requireContext())

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        auth = Firebase.auth

        binding.googleSignIbBtn.setOnClickListener {
            signIn()
        }

//        //google Sign In
//        binding.googleSignIbBtn.setOnClickListener {
//            viewModel.signIn(requireActivity())
//        }

        //google sign Out
        binding.logOutBtn.setOnClickListener {
            viewModel.signOut()
        }

        binding.contactUsView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("RenadOmarQ@gmail.com"))
            }
            val packageManager = requireActivity().packageManager
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Required App is not installed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }



    private fun setLocale(activity: Activity, lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        startActivity(Intent(activity, MainActivity::class.java))
        activity.finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Companion.RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("sign in fragment ", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("sign in fragment", "Google sign in failed", e)
                }
            } else {
                Log.w("sign in fragment", exception.toString())

//    viewModel.checkGoogleSignIn(requestCode,requireActivity(),requireContext(),data)

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(requireContext(),"sign in success ",Toast.LENGTH_SHORT).show()
                    binding.googleSignIbBtn.isVisible = false
                    binding.logOutBtn.isVisible = true
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(requireContext(),"sign in failure ${task.exception}",Toast.LENGTH_SHORT).show()
                    Log.w("sign in fragment", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, Companion.RC_SIGN_IN)
    }

    companion object {
        const val RC_SIGN_IN = 1991
    }
}

