package com.example.gizisight.ui.registrasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

            btnDaftar.setOnClickListener{
                loadingDialog.startLoadingDialog()
                val username = tfUsername.text.toString()
                val email = tfEmail.editText?.text.toString()
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

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}