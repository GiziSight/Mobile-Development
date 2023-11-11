package com.example.gizisight.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gizisight.databinding.ActivityIntroBinding
import com.example.gizisight.ui.login.LoginActivity
import com.example.gizisight.ui.registrasi.RegistrasiActivity
import com.example.gizisight.util.SharedPrefManager

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityIntroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val sharedPref = SharedPrefManager(this)
        val token = sharedPref.getUser()

        if (token != null && token != ""){
            val intent = Intent(this@IntroActivity, HomeActivity::class.java)
            startActivity(intent)
        }
        binding.btnMulai.setOnClickListener{
            val intent = Intent(this@IntroActivity, RegistrasiActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener{
            val intent = Intent(this@IntroActivity, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}