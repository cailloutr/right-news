package com.cailloutr.rightnews.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.cailloutr.rightnews.R
import com.cailloutr.rightnews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    val binding get() = _binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }
}