package com.cailloutr.rightnews.ui.newsfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cailloutr.rightnews.R
import com.cailloutr.rightnews.data.network.service.TheGuardianApi
import com.cailloutr.rightnews.databinding.FragmentNewsBinding
import com.cailloutr.rightnews.enums.ItemNewsType
import com.cailloutr.rightnews.extensions.collectLatestLifecycleFlow
import com.cailloutr.rightnews.extensions.hide
import com.cailloutr.rightnews.extensions.show
import com.cailloutr.rightnews.extensions.snackbar
import com.cailloutr.rightnews.other.Status
import com.cailloutr.rightnews.recyclerview.BannerAdapter
import com.cailloutr.rightnews.ui.CustomItemAnimator
import com.cailloutr.rightnews.ui.chip.ChipItem
import com.cailloutr.rightnews.ui.chip.toChip
import com.cailloutr.rightnews.ui.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "NewsFragment"

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
        binding.newsRecyclerView.itemAnimator = CustomItemAnimator()

//        // Setup Sections Chips
        setupSectionsChipItems()

        // Setup Banner News
        collectLatestLifecycleFlow(viewModel.latestNewsState) {
            when (it.status) {
                Status.LOADING -> {
                    binding.bannersViewPager.hide()
                    binding.shimmerViewPagerLayout.show()
                    binding.shimmerViewPagerLayout.startShimmerAnimation()
                }
                Status.SUCCESS -> {
                    if (binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                        false

                    it.data?.let { newContainer ->
                        binding.shimmerViewPagerLayout.hide()
                        binding.shimmerViewPagerLayout.stopShimmerAnimation()
                        binding.bannersViewPager.show()
                        bannerAdapter.submitList(newContainer.results)
                    }
                }
                Status.ERROR -> {
                    it.message?.let { message ->
                        binding.root.snackbar(message)
                        Log.i(TAG, "Error: $message")
                    }
                }
            }

        }

        // Setup Section's news
        collectLatestLifecycleFlow(viewModel.sectionsNewsState) {
            when (it.status) {
                Status.LOADING -> {
                    binding.newsRecyclerView.hide()
                    binding.shimmerRecyclerviewLayout.show()
                    binding.shimmerRecyclerviewLayout.startShimmerAnimation()
                }
                Status.SUCCESS -> {
                    if (binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                        false

                    it.data?.let { newsList ->
                        binding.shimmerRecyclerviewLayout.hide()
                        binding.shimmerRecyclerviewLayout.stopShimmerAnimation()
                        binding.newsRecyclerView.show()
                        newsAdapter.submitList(newsList)
                    }
                }
                Status.ERROR -> {
                    //TODO
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchDataFromApi()
        }

    }

    private fun setupSectionsChipItems() {
        collectLatestLifecycleFlow(viewModel.sectionsListState) { sections ->
            binding.chipGroup.removeAllViews()

            val listOfSections = sections.data?.listOfSections
            listOfSections?.size?.let { size ->
                repeat(size) {
                    val isChecked = it == 0
                    val chip = ChipItem(
                        id = listOfSections[it].id,
                        text = listOfSections[it].title,
                        isChecked = isChecked
                    ) { id ->
                        viewModel.setSelectedSections(id)
                        viewModel.getNewsBySection()
                    }
                    binding.chipGroup.addView(chip.toChip(requireContext(), binding.chipGroup))
                }
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