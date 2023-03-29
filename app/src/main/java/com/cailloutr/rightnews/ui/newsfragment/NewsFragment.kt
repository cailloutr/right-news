package com.cailloutr.rightnews.ui.newsfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cailloutr.rightnews.R
import com.cailloutr.rightnews.adapters.BannerAdapter
import com.cailloutr.rightnews.data.network.responses.news.toNewsList
import com.cailloutr.rightnews.data.network.service.TheGuardianApi
import com.cailloutr.rightnews.databinding.FragmentNewsBinding
import com.cailloutr.rightnews.enums.ItemNewsType
import com.cailloutr.rightnews.extensions.collectLatestLifecycleFlow
import com.cailloutr.rightnews.ui.chip.ChipItem
import com.cailloutr.rightnews.ui.chip.toChip
import com.cailloutr.rightnews.ui.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//private const val TAG = "NewsFragment"

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()

    @Inject
    lateinit var api: TheGuardianApi

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

        val bannerAdapter = BannerAdapter(ItemNewsType.BANNER) {}

        binding.bannersViewPager.adapter = bannerAdapter
        binding.bannerDots.attachTo(binding.bannersViewPager)

        val newsAdapter = BannerAdapter(ItemNewsType.CATEGORIZED) {}
        binding.newsRecyclerView.adapter = newsAdapter

        setupSectionsChipItems()

        collectLatestLifecycleFlow(viewModel.latestNewsState) {
            newsAdapter.submitList(it?.data?.toNewsList())
        }

        collectLatestLifecycleFlow(viewModel.latestNewsState) {
            bannerAdapter.submitList(it?.data?.toNewsList())
        }

    }

    private fun setupSectionsChipItems() {
        collectLatestLifecycleFlow(viewModel.sectionsState) { sections ->
            binding.chipGroup.removeAllViews()
            repeat(sections.size) {
                val isChecked = it == 0
                val chip = ChipItem(
                    id = sections[it].id,
                    text = sections[it].title,
                    isChecked = isChecked
                )
                binding.chipGroup.addView(chip.toChip(requireContext(), binding.chipGroup))
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