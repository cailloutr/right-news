package com.cailloutr.rightnews.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.cailloutr.rightnews.R
import com.cailloutr.rightnews.databinding.ActivityMainBinding
import com.cailloutr.rightnews.ui.viewmodel.UiStateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityMainBinding
    val binding get() = _binding

    private val uiStateViewModel: UiStateViewModel by viewModels()
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        showUiStateComponents()
    }

    private fun showUiStateComponents() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            title = destination.label

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    uiStateViewModel.components.collect {
                        it.let { hasComponents ->
                            if (hasComponents.bottomNavigation) {
                                binding.bottomNavigation.visibility = View.VISIBLE
                            } else {
                                binding.bottomNavigation.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }
}