package com.cailloutr.rightnews.ui.newsfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cailloutr.rightnews.R
import com.cailloutr.rightnews.adapters.BannerAdapter
import com.cailloutr.rightnews.databinding.FragmentNewsBinding
import com.cailloutr.rightnews.ui.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

private const val TAG = "NewsFragment"

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()

        val adapter = BannerAdapter {}

        binding.bannersViewPager.adapter = adapter
        binding.bannerDots.attachTo(binding.bannersViewPager)

        lifecycleScope.launch {
            viewModel.bannersList
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    adapter.submitList(it)
                }
        }


    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.newsFragment,
                R.id.favoritesFragment,
                R.id.profileFragment
            )
        )

        binding.toolbar.apply {
            setupWithNavController(navController, appBarConfiguration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}