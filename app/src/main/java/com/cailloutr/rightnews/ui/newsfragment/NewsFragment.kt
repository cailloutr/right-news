package com.cailloutr.rightnews.ui.newsfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cailloutr.rightnews.R
import com.cailloutr.rightnews.constants.Constants.NETWORK_ERROR
import com.cailloutr.rightnews.databinding.FragmentNewsBinding
import com.cailloutr.rightnews.enums.ItemNewsType
import com.cailloutr.rightnews.extensions.*
import com.cailloutr.rightnews.model.Article
import com.cailloutr.rightnews.other.Status
import com.cailloutr.rightnews.recyclerview.BannerAdapter
import com.cailloutr.rightnews.ui.CustomItemAnimator
import com.cailloutr.rightnews.ui.chip.ChipItem
import com.cailloutr.rightnews.ui.chip.toChip
import com.cailloutr.rightnews.ui.viewmodel.NewsViewModel
import com.cailloutr.rightnews.ui.viewmodel.UiStateViewModel
import com.cailloutr.rightnews.ui.viewmodel.VisualComponents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

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

        // Setup Banner Article
        setupBannerNews(bannerAdapter)

        // Setup Section's news
        setupSectionsNews(newsAdapter)

        setupRefreshFunction()

        showSnackBar()

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.setShouldRefresh(true)
        }

        binding.fragmentNewsSeeAll.setOnClickListener {
            navigateToLatestNewsFragment()
        }

        binding.fragmentNewsAllSections.setOnClickListener {
            navigateToAllSectionsFragment()
        }

        binding.searchBarEdittext.setOnClickListener {
            findNavController().navigate(
                NewsFragmentDirections.actionNewsFragmentToSearchFragment()
            )
        }
    }

    private fun showSnackBar() {
        collectLifecycleFlow(viewModel.showSnackBarEvent) { event ->
            event.getContentIfNotHandled()?.let { messageResId ->
                binding.root.snackbar(getString(messageResId))
            }
        }
    }

    private fun setupRefreshFunction() {
        collectLatestLifecycleFlow(viewModel.isRefreshing) {
            if (it) {
                viewModel.fetchDataFromApi() { response ->
                    when (response.status) {
                        Status.SUCCESS -> {
                            viewModel.showSnackBarMessage(R.string.network_status_success)
                        }
                        Status.ERROR -> {
                            if (response.message == NETWORK_ERROR) {
                                viewModel.showSnackBarMessage(R.string.network_error)
                            } else {
                                viewModel.showSnackBarMessage(R.string.unknown_error)
                            }
                        }
                        else -> {}
                    }
                }
                viewModel.setShouldRefresh(false)
            } else {
                if (binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                    false
            }
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
        collectLifecycleFlow(viewModel.sectionsNewsState) {
            binding.newsRecyclerView.hide()
            binding.shimmerLayout.show()
            binding.progressBar2.show()
            binding.shimmerLayout.startShimmerAnimation()

            it?.results?.let { newsList ->
                binding.shimmerLayout.hide()
                binding.shimmerLayout.stopShimmerAnimation()
                binding.progressBar2.hide()
                binding.progressBar2.setVisibilityAfterHide(View.GONE)

                if (newsList.isNotEmpty()) {
                    binding.newsRecyclerView.show()
                    newsAdapter.submitList(newsList)
                }
            }
        }
    }

    private fun setupBannerNews(bannerAdapter: BannerAdapter) {
        collectLifecycleFlow(viewModel.latestNewsState) {
            binding.bannersViewPager.hide()
            binding.shimmerViewPagerLayout.show()
            binding.shimmerViewPagerLayout.startShimmerAnimation()

            it?.results?.let { articlesList ->
                binding.shimmerViewPagerLayout.hide()
                binding.shimmerViewPagerLayout.stopShimmerAnimation()
                binding.bannersViewPager.show()
                bannerAdapter.submitList(articlesList)
            }
        }
    }

    private fun setupSectionsChipItems() {
        collectLifecycleFlow(viewModel.sectionsListState) { sections ->
            binding.chipGroup.removeAllViews()

            val selectedSection = viewModel.selectedSectionsState.first()
            repeat(sections.size) {
                val isChecked = sections[it].id == selectedSection
                val chip = ChipItem(
                    id = sections[it].id,
                    text = sections[it].title,
                    isChecked = isChecked
                ) { id ->
                    if (viewModel.selectedSectionsState.value != id) {
                        viewModel.setSelectedSections(id)
                        viewModel.getNewsBySection() { response ->
                            when (response.status) {
                                Status.ERROR -> {
                                    if (response.message == NETWORK_ERROR) {
                                        viewModel.showSnackBarMessage(R.string.network_error)
                                    } else {
                                        viewModel.showSnackBarMessage(R.string.unknown_error)
                                    }
                                }
                                else -> {}
                            }
                        }
                        binding.progressBar2.show()
                    }
                }
                binding.chipGroup.addView(chip.toChip(requireContext(), binding.chipGroup))
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
            NewsFragmentDirections.actionNewsFragmentToLatestNewsFragment(null, null)
        )
    }

    private fun navigateToDetailsFragment(it: Article) {
        findNavController().navigate(
            NewsFragmentDirections.actionNewsFragmentToNewsDetailsFragment(it)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}