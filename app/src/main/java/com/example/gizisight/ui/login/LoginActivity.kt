package com.example.gizisight.ui.login

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.example.gizisight.R
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.data.Result
import com.example.gizisight.databinding.ActivityLoginBinding
import com.example.gizisight.databinding.ActivityRegistrasiBinding
import com.example.gizisight.ui.DataDiriActivity
import com.example.gizisight.ui.HomeActivity
import com.example.gizisight.ui.registrasi.RegistrasiActivity
import com.example.gizisight.ui.registrasi.RegistrasiViewModel
import com.example.gizisight.util.LoadingDialog
import com.example.gizisight.util.SharedPrefManager
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient : GoogleSignInClient
    private lateinit var mAuth : FirebaseAuth
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this@LoginActivity)
    private val viewModel : LoginViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val userPref = SharedPrefManager(this)
        val loadingDialog: LoadingDialog = LoadingDialog(this@LoginActivity)
        supportActionBar?.hide()

//        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

//        supportActionBar?.setCustomView(R.layout.toolbar_title_layout)



        binding?.apply {
            btnLogin.setOnClickListener{
                loadingDialog.startLoadingDialog()
                val email = tfEmail.editText?.text
                val password = tfPassword.editText?.text
                viewModel.loginUser(email.toString(), password.toString(), this@LoginActivity, userPref, loadingDialog)
            }

            txtInputEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // Do Nothing
                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    // Validate the email format and show error if needed
                    if (!isValidEmail(s.toString())) {
                        tfEmail.error = "Invalid email format"
                    } else {
                        tfEmail.error = null // Clear the error
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    // Do nothing
                }
            })
        }

        binding?.btnDaftar?.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrasiActivity::class.java)
            startActivity(intent)
        }

        binding?.btnBack?.setOnClickListener {
            finish()
        }

        var gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1075489775291-9dgf69t2a891vhmtgaqlvosfth591j3b.apps.googleusercontent.com")
                .requestEmail()
                .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        var signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("1075489775291-9dgf69t2a891vhmtgaqlvosfth591j3b.apps.googleusercontent.com")
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build()
            )
            .build()

        // Initialize Firebase Auth
        auth = Firebase.auth

        binding?.btnGoogle?.setOnClickListener {
            googleSignIn()
        }
    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val sharedPref = SharedPrefManager(this)
        val email = sharedPref.getEmail()
        if( currentUser != null ){

            val em = currentUser.email

            viewModel.getUser(em!!, sharedPref).observe(this) {result ->
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
            viewModel.getUser(email!!, sharedPref).observe(this) {result ->
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
    }


    fun googleSignIn(){
        var intent = mGoogleSignInClient.signInIntent
        startActivityForResult(intent,  1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                var account : GoogleSignInAccount = task.getResult(ApiException::class.java)
                // Get user information

                firebaseAuthWithGoogle(account)

//                account.idToken?.let { firebaseAuthWithGoogle(it) }

            } catch (e : ApiException) {
                Log.d("ERRREROR", e.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        // Got an ID token from Google. Use it to authenticate
        // with Firebase.
        val firebaseCredential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(firebaseCredential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LIGNGER", "signInWithCredential:success")
                    val user = auth.currentUser
                    val intent = Intent(applicationContext, DataDiriActivity::class.java)
                    intent.putExtra("displayName", account.displayName)
                    intent.putExtra("email", account.email)
                    intent.putExtra("username", extractUsername(account.email))
                    intent.putExtra("photoUrl", extractUsername(account.photoUrl.toString()))
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.d("LIGNGER", "signInWithCredential:failure", task.exception)
//                    startActivity(Intent(applicationContext, DataDiriActivity::class.java))
                }
            }
    }

    // Function to validate email format
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun extractUsername(email: String?): String? {
        val regex = "^(.+)@".toRegex()
        val matchResult = email?.let { regex.find(it) }

        return matchResult?.groupValues?.get(1)
    }
}