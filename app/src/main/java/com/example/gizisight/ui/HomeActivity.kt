package com.example.gizisight.ui

import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.gizisight.R
import com.example.gizisight.ViewModelFactory
import com.example.gizisight.databinding.ActivityHomeBinding
import com.example.gizisight.ui.fragment.home.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var isBackPressedOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if (isBackPressedOnce) {
            super.onBackPressed()
            return
        }

        Toast.makeText(this, "Tekan tombol kembali dua kali untuk keluar dari aplikasi", Toast.LENGTH_SHORT).show()

        isBackPressedOnce = true;

        Handler().postDelayed({
            isBackPressedOnce = false
        }, 200)

    }
}