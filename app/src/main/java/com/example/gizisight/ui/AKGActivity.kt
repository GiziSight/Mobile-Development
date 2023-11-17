package com.example.gizisight.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.gizisight.R
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.data.Result
import com.example.gizisight.databinding.ActivityAkgactivityBinding
import com.example.gizisight.databinding.ActivityLoginBinding
import com.example.gizisight.databinding.ActivityRegistrasiBinding
import com.example.gizisight.ui.login.LoginViewModel
import com.example.gizisight.util.SharedPrefManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class AKGActivity : AppCompatActivity() {
    private var _binding: ActivityAkgactivityBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAkgactivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@AKGActivity)
        val viewModel: AKGViewModel by viewModels {
            factory
        }

        val sharedPref = SharedPrefManager(this)
        val emailPref = sharedPref.getEmail()

        val acct = GoogleSignIn.getLastSignedInAccount(this)
        var email = Firebase.auth.currentUser?.email

        binding?.btnBack?.setOnClickListener{
            finish()
        }

        if( !emailPref.isNullOrEmpty()) {
            viewModel.getUser(emailPref, sharedPref).observe(this){ result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
//                            Toast.makeText(
//                                requireActivity(),
//                                "Loading..",
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                        is Result.Success -> {
                            binding?.apply {
                                tvName.text = result.data.username
                                tvUmur.text = "${result.data.age} Tahun"
                                tvTinggi.text = "${result.data.height} cm"
                                tvBerat.text = "${result.data.weight} kg"

                            }
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

        } else if (!email.isNullOrEmpty()) {
            viewModel.getUser(email.toString(), sharedPref).observe(this){ result ->
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
                            binding?.apply {
                                if (acct != null) {
                                    val personName = acct!!.displayName
                                    binding?.tvName?.text = personName
                                    tvUmur.text = "${result.data.age} Tahun"
                                    tvTinggi.text = "${result.data.height} cm"
                                    tvBerat.text = "${result.data.weight} kg"
                                } else {
                                    tvName.text = result.data.username
                                    tvUmur.text = "${result.data.age} Tahun"
                                    tvTinggi.text = "${result.data.height} cm"
                                    tvBerat.text = "${result.data.weight} kg"
                                }

                            }
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
}