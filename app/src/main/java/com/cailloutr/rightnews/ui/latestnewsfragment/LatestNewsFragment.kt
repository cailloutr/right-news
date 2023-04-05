package com.cailloutr.rightnews.ui.latestnewsfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.cailloutr.rightnews.databinding.FragmentLatestNewsBinding
import com.cailloutr.rightnews.enums.ItemNewsType
import com.cailloutr.rightnews.extensions.*
import com.cailloutr.rightnews.other.Status
import com.cailloutr.rightnews.recyclerview.BannerAdapter
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


    private val latestNewsViewModel: LatestNewsViewModel by viewModels()
    private val uiStateViewModel: UiStateViewModel by activityViewModels()

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

        val newsAdapter = BannerAdapter(ItemNewsType.CATEGORIZED) {}
        binding.latestNewsRecyclerview.adapter = newsAdapter
        binding.latestNewsRecyclerview.itemAnimator = CustomItemAnimator()

        collectLatestLifecycleFlow(latestNewsViewModel.latestNewsState) { result ->
            when (result.status) {
                Status.LOADING -> {
                    binding.latestNewsRecyclerview.hide()
                    binding.shimmerRecyclerviewLayout.show()
                    binding.shimmerRecyclerviewLayout.startShimmerAnimation()
                }
                Status.SUCCESS -> {
                    if (binding.swipeRefreshLayout.isRefreshing) binding.swipeRefreshLayout.isRefreshing =
                        false


                    result.data?.let { newContainer ->
                        binding.shimmerRecyclerviewLayout.hide()
                        binding.shimmerRecyclerviewLayout.startShimmerAnimation()
                        binding.latestNewsRecyclerview.show()
                        newsAdapter.submitList(newContainer.results)
                    }
                }
                Status.ERROR -> {
                    result.message?.let { message ->
                        binding.root.snackbar(message)
                        Log.i(TAG, "Error: $message")
                    }
                }
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            latestNewsViewModel.fetchDataFromApi()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}