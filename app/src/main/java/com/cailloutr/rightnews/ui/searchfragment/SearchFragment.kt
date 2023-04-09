package com.cailloutr.rightnews.ui.searchfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cailloutr.rightnews.R
import com.cailloutr.rightnews.constants.Constants
import com.cailloutr.rightnews.databinding.FragmentSearchBinding
import com.cailloutr.rightnews.enums.ItemNewsType
import com.cailloutr.rightnews.extensions.*
import com.cailloutr.rightnews.other.Status
import com.cailloutr.rightnews.recyclerview.BannerAdapter
import com.cailloutr.rightnews.ui.CustomItemAnimator
import com.cailloutr.rightnews.ui.viewmodel.SearchViewModel
import com.cailloutr.rightnews.ui.viewmodel.UiStateViewModel
import com.cailloutr.rightnews.ui.viewmodel.VisualComponents
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "SearchFragment"

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    val binding get() = _binding!!

    private val uiStateViewModel: UiStateViewModel by activityViewModels()
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(
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

        val adapter = setupAdapter()

        setupSearchResult(adapter)

        binding.searchBarEdittext.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.editableText.toString()
                viewModel.getSearchResult(query)
            }
            return@setOnEditorActionListener true
        }
    }

    private fun setupSearchResult(adapter: BannerAdapter) {
        collectLatestLifecycleFlow(viewModel.searchResultState) { searchResult ->
            when (searchResult?.status) {
                Status.LOADING -> {
                    showProgressBar()
                }
                Status.SUCCESS -> {
                    binding.progressBar.hide()

                    searchResult.data?.let { searchResultData ->
                        val resultList = searchResultData.results

                        if (resultList.isNotEmpty()) {
                            binding.newsRecyclerView.show()
                            adapter.submitList(resultList)
                        } else {
                            showPlaceholderView(
                                R.drawable.ic_no_results_24,
                                getString(R.string.no_results_found)
                            )
                        }
                    }
                }
                Status.ERROR -> {
                    searchResult.message?.let { message ->
                        binding.root.snackbar(Constants.NETWORK_ERROR_MESSAGE)
                        Log.e(TAG, "Error: $message")
                    }
                    showPlaceholderView(
                        R.drawable.ic_no_results_24,
                        getString(R.string.no_results_found)
                    )
                }
                else -> {
                    showPlaceholderView(
                        R.drawable.ic_search_24,
                        getString(R.string.search_content)
                    )
                }
            }
        }
    }

    private fun showProgressBar() {
        binding.newsRecyclerView.hide()
        binding.placeholderContainer.hide()
        binding.progressBar.show()
    }

    private fun showPlaceholderView(@DrawableRes drawable: Int, text: String) {
        binding.newsRecyclerView.hide()
        binding.progressBar.hide()
        binding.placeholderContainer.show()
        setupPlaceholderView(drawable, text)
    }

    private fun setupAdapter(): BannerAdapter {
        val adapter = BannerAdapter(ItemNewsType.CATEGORIZED) {
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToNewsDetailsFragment(it)
            )
        }
        binding.newsRecyclerView.adapter = adapter
        binding.newsRecyclerView.itemAnimator = CustomItemAnimator()
        return adapter
    }

    private fun setupPlaceholderView(drawable: Int, text: String) {
        binding.placeholderImage.apply {
            setImageDrawable(
                ContextCompat.getDrawable(requireContext(), drawable)
            )
        }
        binding.placeholderText.text = text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}