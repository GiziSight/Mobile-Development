package com.example.gizisight.ui.registrasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.example.gizisight.R
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.databinding.ActivityRegistrasiBinding
import com.example.gizisight.ui.HomeActivity
import com.example.gizisight.util.LoadingDialog

class RegistrasiActivity : AppCompatActivity() {
    private var _binding: ActivityRegistrasiBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRegistrasiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.hide()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@RegistrasiActivity)
        val viewModel: RegistrasiViewModel by viewModels {
            factory
        }
        val loadingDialog: LoadingDialog = LoadingDialog(this@RegistrasiActivity)
        binding?.btnBack?.setOnClickListener {
            finish()
        }

        binding?.apply {
            btnDaftar.setOnClickListener{
                loadingDialog.startLoadingDialog()
                val username = tfUsername.text.toString()
                val email = tfEmail.text.toString()
                val password = tfPassword.text.toString()
                val gender = autoCompleteTextView.text.toString()
                val age = tfUmur.text.toString()
                val height = tfTinggi.text.toString()
                val weight = tfBeratBadan.text.toString()

//                Log.d("REfefee", gender.toString())

                viewModel.registerUser(email, username, password, gender, age, height, weight, this@RegistrasiActivity, loadingDialog )

            }
        }


        val feelings = resources.getStringArray(R.array.simple_items)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, feelings)
        binding?.autoCompleteTextView?.setAdapter(arrayAdapter)

    }
}