package com.example.gizisight.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.data.Result
import com.example.gizisight.databinding.ActivityIntroBinding
import com.example.gizisight.databinding.ActivityLoginBinding
import com.example.gizisight.ui.login.LoginActivity
import com.example.gizisight.ui.login.LoginViewModel
import com.example.gizisight.ui.registrasi.RegistrasiActivity
import com.example.gizisight.util.SharedPrefManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class IntroActivity : AppCompatActivity() {
    private var _binding: ActivityIntroBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this@IntroActivity)
    private val viewModel: IntroViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        // Initialize Firebase Auth
        auth = Firebase.auth

        val currentUser = auth.currentUser
        val sharedPref = SharedPrefManager(this)
        val email = sharedPref.getEmail()
        if (currentUser != null) {

            val em = currentUser.email

            viewModel.getUser(em!!, sharedPref).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
//                            Toast.makeText(
//                                this,
//                                "Loading..",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                        is Result.Success -> {
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        is Result.Error -> {
                            Toast.makeText(
                                this,
                                result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        } else {
            viewModel.getUser(email!!, sharedPref).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
//                            Toast.makeText(
//                                this,
//                                "Loading..",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                        is Result.Success -> {
                            val intent = Intent(applicationContext, HomeActivity::class.java)
                            startActivity(intent)
                        }
                        is Result.Error -> {
                            Toast.makeText(
                                this,
                                result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding?.btnMulai?.setOnClickListener {
            val intent = Intent(this@IntroActivity, RegistrasiActivity::class.java)
            startActivity(intent)
        }

        binding?.btnLogin?.setOnClickListener {
            val intent = Intent(this@IntroActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

}