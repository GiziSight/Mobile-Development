package com.example.gizisight.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gizisight.R
import com.example.gizisight.databinding.ActivityAkgactivityBinding
import com.example.gizisight.databinding.ActivityLoginBinding
import com.example.gizisight.databinding.ActivityRegistrasiBinding

class AKGActivity : AppCompatActivity() {
    private var _binding: ActivityAkgactivityBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAkgactivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

    }
}