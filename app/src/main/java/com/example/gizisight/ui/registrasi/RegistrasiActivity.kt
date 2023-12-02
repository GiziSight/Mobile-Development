package com.example.gizisight.ui.registrasi

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import com.example.gizisight.R
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.databinding.ActivityRegistrasiBinding
import com.example.gizisight.ui.login.LoginActivity
import com.example.gizisight.util.LoadingDialog
import com.example.gizisight.util.SharedPrefManager
import java.util.*

class RegistrasiActivity : AppCompatActivity() {
    private var _binding: ActivityRegistrasiBinding? = null
    private val binding get() = _binding
    private var tanggalLahir: String? = null

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

        val sharedPref = SharedPrefManager(this)

        binding?.apply {
            tfUmur.setOnClickListener {
                showDatePicker()
            }
            txtInputEmail.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // Do Nothing
                }

                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // Validate the email format and show error if needed
                    if (!isValidEmail(s.toString())) {
                        tfEmail.error = "Format email salah"
                    } else {
                        tfEmail.error = null // Clear the error
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                    // Do nothing
                }
            })



            btnDaftar.setOnClickListener {
                loadingDialog.startLoadingDialog()
                val username = tfUsername.text.toString()
                val email = tfEmail.editText?.text.toString()
                val password = tfPassword.text.toString()
                val gender = autoCompleteTextView.text.toString()
                val age = tfUmur.text.toString()
                val height = tfTinggi.text.toString()
                val weight = tfBeratBadan.text.toString()

                // Convert the selected gender to English if it's in another language
                val englishGender = when (gender.toLowerCase()) {
                    "perempuan" -> "Female"
                    else -> "Male"
                }

//                Log.d("REfefee", gender.toString())

                if (!tanggalLahir.isNullOrEmpty() && !username.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty() && !gender.isNullOrEmpty() && !height.isNullOrEmpty() && !weight.isNullOrEmpty()) {
                    viewModel.registerUser(
                        email,
                        username,
                        password,
                        englishGender,
                        tanggalLahir!!,
                        height,
                        weight,
                        this@RegistrasiActivity,
                        loadingDialog,
                        sharedPref
                    )
                } else {
                    Toast.makeText(
                        this@RegistrasiActivity,
                        "Silahkan isi field yang masih kosong!",
                        Toast.LENGTH_SHORT
                    ).show()
                    loadingDialog.dismiss()
                }


            }
        }

        val feelings = resources.getStringArray(R.array.simple_items)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, feelings)
        binding?.autoCompleteTextView?.setAdapter(arrayAdapter)

        binding?.btnMasuk?.setOnClickListener {
            val intent = Intent(this@RegistrasiActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Function to show the DatePickerDialog
    fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Set the selected date to the EditText
                binding?.tfUmur?.setText("$selectedDay/${selectedMonth + 1}/$selectedYear")
                tanggalLahir = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

}