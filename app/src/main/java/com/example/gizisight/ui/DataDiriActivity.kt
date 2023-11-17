package com.example.gizisight.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.example.gizisight.R
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.data.Result
import com.example.gizisight.databinding.ActivityDataDiriBinding
import com.example.gizisight.databinding.ActivityLoginBinding
import com.example.gizisight.databinding.ActivityRegistrasiBinding
import com.example.gizisight.ui.registrasi.RegistrasiViewModel
import com.example.gizisight.util.LoadingDialog
import com.example.gizisight.util.SharedPrefManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class DataDiriActivity : AppCompatActivity() {
    private var _binding: ActivityDataDiriBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDataDiriBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@DataDiriActivity)
        val viewModel: DataDiriViewModel by viewModels {
            factory
        }

        val currentUser = GoogleSignIn.getLastSignedInAccount(this)
        if( currentUser != null ){
            val sharedPref = SharedPrefManager(this)
            val em = currentUser.email

            viewModel.getUser(em!!, sharedPref).observe(this) {result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            Toast.makeText(
                                this,
                                "Loading..",
                                Toast.LENGTH_SHORT
                            ).show()
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

        val feelings = resources.getStringArray(R.array.simple_items)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, feelings)
        binding?.autoCompleteTextView?.setAdapter(arrayAdapter)

        val loadingDialog = LoadingDialog(this@DataDiriActivity)

        // Retrieve data from the intent
        val email = intent.getStringExtra("email")
        val username = intent.getStringExtra("username")
        val displayName = intent.getStringExtra("displayName")
        val photoUrl = intent.getStringExtra("photoUrl")

        binding?.apply {
            btnSubmit.setOnClickListener{
                loadingDialog.startLoadingDialog()
                val gender = autoCompleteTextView.text.toString()
                val age = tfUmur.editText?.text.toString()
                val height = tfTinggi.editText?.text.toString()
                val weight = tfBeratBadan.editText?.text.toString()

                viewModel.registerUser(email!!, username!!, "123123", gender, age, height, weight, this@DataDiriActivity, loadingDialog )

            }
        }


        Log.d("sdasf1", email.toString())


        binding?.btnBack?.setOnClickListener {
            finish()
        }


    }
}