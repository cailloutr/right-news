package com.cailloutr.rightnews.ui.newsfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cailloutr.rightnews.constants.Constants.NETWORK_ERROR_MESSAGE
import com.cailloutr.rightnews.databinding.FragmentNewsBinding
import com.cailloutr.rightnews.enums.ItemNewsType
import com.cailloutr.rightnews.extensions.*
import com.cailloutr.rightnews.model.News
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.other.Status
import com.cailloutr.rightnews.recyclerview.BannerAdapter
import com.cailloutr.rightnews.ui.CustomItemAnimator
import com.cailloutr.rightnews.ui.chip.ChipItem
import com.cailloutr.rightnews.ui.chip.toChip
import com.cailloutr.rightnews.ui.viewmodel.NewsViewModel
import com.cailloutr.rightnews.ui.viewmodel.UiStateViewModel
import com.cailloutr.rightnews.ui.viewmodel.VisualComponents
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "NewsFragment"

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    val binding get() = _binding!!

    private val viewModel: NewsViewModel by viewModels()
    private val uiStateViewModel: UiStateViewModel by activityViewModels()

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
        uiStateViewModel.hasComponents = VisualComponents(bottomNavigation = true)
        setupToolbar(binding.toolbar)

        val bannerAdapter = setupBannerViewPager()

        val newsAdapter = setupNewRecyclerview()

        // Setup Sections Chips
        setupSectionsChipItems()

        // Setup Banner News
        setupBannerNews(bannerAdapter)

        // Setup Section's news
        setupSectionsNews(newsAdapter)

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchDataFromApi()
        }

        binding.fragmentNewsSeeAll.setOnClickListener {
            navigateToLatestNewsFragment()
        }

        binding.fragmentNewsAllSections.setOnClickListener {
            navigateToAllSectionsFragment()
        }


        binding.searchBarEdittext.setOnClickListener {
//            Toast.makeText(requireContext(), "Click", Toast.LENGTH_SHORT).show()
            findNavController().navigate(
                NewsFragmentDirections.actionNewsFragmentToSearchFragment()
            )
        }
    }

    private fun setupNewRecyclerview(): BannerAdapter {
        val newsAdapter = BannerAdapter(ItemNewsType.CATEGORIZED) {
            navigateToDetailsFragment(it)
        }
        binding.newsRecyclerView.adapter = newsAdapter
        binding.newsRecyclerView.itemAnimator = CustomItemAnimator()
        return newsAdapter
    }

    private fun setupBannerViewPager(): BannerAdapter {
        val bannerAdapter = BannerAdapter(ItemNewsType.BANNER) {
            navigateToDetailsFragment(it)
        }

        binding.bannersViewPager.adapter = bannerAdapter
        binding.bannerDots.attachTo(binding.bannersViewPager)
        return bannerAdapter
    }

    private fun setupSectionsNews(newsAdapter: BannerAdapter) {
        collectLatestLifecycleFlow(viewModel.sectionsNewsState) {
            when (it.status) {
                Status.LOADING -> {
                    binding.newsRecyclerView.hide()
                    binding.shimmerLayout.show()
                    binding.shimmerLayout.startShimmerAnimation()
                }
                Status.SUCCESS -> {
                    if (binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                        false

                    it.data?.let { newsList ->
                        binding.shimmerLayout.hide()
                        binding.shimmerLayout.stopShimmerAnimation()

                        if (newsList.results.isNotEmpty()) {
                            binding.newsRecyclerView.show()
                            newsAdapter.submitList(newsList.results)
                        }
                    }
                }
                Status.ERROR -> {
                    showErrorMessage(it)
                }
            }
        }
    }

    private fun showErrorMessage(it: Resource<NewsContainer>) {
        it.message?.let { message ->
            binding.root.snackbar(NETWORK_ERROR_MESSAGE)
            Log.e(TAG, "Error: $message")
        }
    }

    private fun setupBannerNews(bannerAdapter: BannerAdapter) {
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
                    showErrorMessage(it)
                }
            }

        }
    }

    private fun navigateToAllSectionsFragment() {
        findNavController().navigate(
            NewsFragmentDirections.actionNewsFragmentToAllSectionsFragment()
        )
    }

    private fun navigateToLatestNewsFragment() {
        findNavController().navigate(
            NewsFragmentDirections.actionNewsFragmentToLatestNewsFragment(null)
        )
    }

    private fun navigateToDetailsFragment(it: News) {
        findNavController().navigate(
            NewsFragmentDirections.actionNewsFragmentToNewsDetailsFragment(it)
        )
    }

    private fun setupSectionsChipItems() {
        collectLatestLifecycleFlow(viewModel.selectedSectionsState) { selectedSection ->
            collectLatestLifecycleFlow(viewModel.sectionsListState) { sections ->
                binding.chipGroup.removeAllViews()

                val listOfSections = sections.data?.listOfSections
                listOfSections?.size?.let { size ->
                    repeat(size) {
                        val isChecked = listOfSections[it].id == selectedSection
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}