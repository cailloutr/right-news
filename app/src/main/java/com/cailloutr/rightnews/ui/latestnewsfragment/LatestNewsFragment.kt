package com.cailloutr.rightnews.ui.latestnewsfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.cailloutr.rightnews.R
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.constants.Constants.LATEST_NEWS
import com.cailloutr.rightnews.constants.Constants.NETWORK_ERROR_MESSAGE
import com.cailloutr.rightnews.databinding.FragmentLatestNewsBinding
import com.cailloutr.rightnews.enums.ItemNewsType
import com.cailloutr.rightnews.extensions.*
import com.cailloutr.rightnews.model.Article
import com.cailloutr.rightnews.model.NewsContainer
import com.cailloutr.rightnews.model.SectionWrapper
import com.cailloutr.rightnews.other.Resource
import com.cailloutr.rightnews.other.Status
import com.cailloutr.rightnews.recyclerview.NewsAdapter
import com.cailloutr.rightnews.ui.CustomItemAnimator
import com.cailloutr.rightnews.ui.viewmodel.LatestNewsViewModel
import com.cailloutr.rightnews.ui.viewmodel.UiStateViewModel
import com.cailloutr.rightnews.ui.viewmodel.VisualComponents
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "LatestNewsFragment"

@AndroidEntryPoint
class LatestNewsFragment : Fragment() {

    private var _binding: FragmentLatestNewsBinding? = null
    val binding get() = _binding!!

    private val args: LatestNewsFragmentArgs by navArgs()

    private val latestNewsViewModel: LatestNewsViewModel by viewModels()
    private val uiStateViewModel: UiStateViewModel by activityViewModels()

    private lateinit var refreshNews: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLatestNewsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiStateViewModel.hasComponents = VisualComponents()
        setupToolbar(binding.toolbar)

        if (args.sectionId != null) {
            binding.toolbar.title = args.sectionTitle
            getNewsOfSection()
            refreshNews = { getNewsOfSection() }
        } else {
            getLatestNews()
            refreshNews = { getLatestNews() }
        }

        val newsAdapter = setupAdapter()
        binding.latestNewsRecyclerview.adapter = newsAdapter

        /*lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsAdapter.loadStateFlow.collect {
                    binding.prependProgress.isVisible = it.source.prepend is LoadState.Loading
                    binding.appendProgress.isVisible = it.source.append is LoadState.Loading
                }
            }
        }*/

        setupNews(newsAdapter)

        setupRefreshFunction()

        showSnackBar()

        /*collectLatestLifecycleFlow(latestNewsViewModel.latestNewsState) { result ->
            when (result.status) {
                Status.LOADING -> {
                    binding.latestNewsRecyclerview.hide()
                    binding.placeholderContainer.hide()
                    binding.shimmerLayout.show()
                    binding.shimmerLayout.startShimmerAnimation()
                }
                Status.SUCCESS -> {
                    if (binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                        false

                    result.data?.let { newContainer ->
                        binding.shimmerLayout.hide()
                        binding.shimmerLayout.stopShimmerAnimation()

                        val newsContainerResult = newContainer.results
                        if (newsContainerResult.isEmpty()) {
                            binding.placeholderContainer.show()
                        } else {
                            binding.latestNewsRecyclerview.show()
                            newsAdapter.submitData(newsContainerResult)
                        }
                    }
                }
                Status.ERROR -> {
                    showErrorMessage(result)
                }
            }
        }*/

        binding.swipeRefreshLayout.setOnRefreshListener {
            latestNewsViewModel.setShouldRefresh(true)
        }

    }

    private fun showSnackBar() {
        collectLifecycleFlow(latestNewsViewModel.showSnackBarEvent) { event ->
            event.getContentIfNotHandled()?.let { messageResId ->
                binding.root.snackbar(getString(messageResId))
            }
        }
    }

    private fun setupRefreshFunction() {
        collectLatestLifecycleFlow(latestNewsViewModel.isRefreshing) {
            if (it) {
                refreshNews()
                latestNewsViewModel.setShouldRefresh(false)
            } else {
                if (binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                    false
            }
        }
    }

    private fun setupNews(newsAdapter: NewsAdapter) {
        collectLatestLifecycleFlow(latestNewsViewModel.latestNewsState) {
            binding.latestNewsRecyclerview.hide()
            binding.placeholderContainer.hide()
            binding.shimmerLayout.show()
            binding.shimmerLayout.startShimmerAnimation()

            if (it?.total == 0L) {
                binding.shimmerLayout.hide()
                binding.shimmerLayout.stopShimmerAnimation()
                binding.placeholderContainer.show()
                newsAdapter.submitList(it.results)
            } else {
                binding.shimmerLayout.hide()
                binding.shimmerLayout.stopShimmerAnimation()
                binding.latestNewsRecyclerview.show()
                newsAdapter.submitList(it?.results)
            }
        }
    }

    private fun getLatestNews() {
        latestNewsViewModel.getNewsOfSection(
            SectionWrapper(
                LATEST_NEWS,
                ""
            )
        ) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    latestNewsViewModel.showSnackBarMessage(R.string.network_status_success)
                }
                Status.ERROR -> {
                    if (response.message == Constants.NETWORK_ERROR) {
                        latestNewsViewModel.showSnackBarMessage(R.string.network_error)
                    } else {
                        latestNewsViewModel.showSnackBarMessage(R.string.unknown_error)
                    }
                }
                else -> {}
            }
        }
    }

    private fun getNewsOfSection() {
        latestNewsViewModel.getNewsOfSection(
            SectionWrapper(
                args.sectionId!!.lowercase(),
                args.sectionId!!.lowercase()
            )
        ) {}
    }

    private fun showErrorMessage(result: Resource<NewsContainer>) {
        result.message?.let { message ->
            binding.root.snackbar(NETWORK_ERROR_MESSAGE)
            Log.e(TAG, "Error: $message")
        }
    }

    private fun setupAdapter(): NewsAdapter {
        val newsAdapter = NewsAdapter(ItemNewsType.CATEGORIZED) {
            navigateToNewsDetailsFragment(it)
        }
        /*binding.latestNewsRecyclerview.adapter = newsAdapter.withLoadStateHeaderAndFooter(
            header = NewsLoadStateAdapter { newsAdapter.retry() },
            footer = NewsLoadStateAdapter { newsAdapter.retry() },
        )
        lifecycleScope.launch {
            newsAdapter.loadStateFlow.collect { loadState ->
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && newsAdapter.itemCount == 0
                // show empty list
                binding.placeholderContainer.isVisible = isListEmpty
                // Only show the list if refresh succeeds.
                binding.latestNewsRecyclerview.isVisible = !isListEmpty

                // Show loading spinner during initial load or refresh.
                binding.shimmerLayout.isVisible = loadState.source.refresh is LoadState.Loading
            }
        }*/

        binding.latestNewsRecyclerview.itemAnimator = CustomItemAnimator()
        return newsAdapter
    }

    private fun navigateToNewsDetailsFragment(it: Article) {
        findNavController().navigate(
            LatestNewsFragmentDirections.actionLatestNewsFragmentToNewsDetailsFragment(it)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}