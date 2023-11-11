package com.example.gizisight.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gizisight.databinding.ActivityDataDiriBinding
import com.example.gizisight.databinding.ActivityLoginBinding
import com.example.gizisight.databinding.ActivityRegistrasiBinding

class DataDiriActivity : AppCompatActivity() {
    private var _binding: ActivityDataDiriBinding? = null
    private val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDataDiriBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.btnSubmit?.setOnClickListener{
            val intent = Intent(this@DataDiriActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        binding?.btnBack?.setOnClickListener{
            finish()
        }



    }
}