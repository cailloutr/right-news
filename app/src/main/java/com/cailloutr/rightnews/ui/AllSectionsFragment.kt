package com.cailloutr.rightnews.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.cailloutr.rightnews.constants.Constants.NETWORK_ERROR_MESSAGE
import com.cailloutr.rightnews.databinding.FragmentAllSectionsBinding
import com.cailloutr.rightnews.extensions.collectLatestLifecycleFlow
import com.cailloutr.rightnews.extensions.setupToolbar
import com.cailloutr.rightnews.extensions.snackbar
import com.cailloutr.rightnews.model.toAllSectionsItem
import com.cailloutr.rightnews.other.Status
import com.cailloutr.rightnews.recyclerview.HeadlineAdapter
import com.cailloutr.rightnews.ui.viewmodel.AllSectionsViewModel
import com.cailloutr.rightnews.ui.viewmodel.UiStateViewModel
import com.cailloutr.rightnews.ui.viewmodel.VisualComponents
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AllSectionsFragment"

@AndroidEntryPoint
class AllSectionsFragment : Fragment() {

    private var _binding: FragmentAllSectionsBinding? = null
    val binding get() = _binding!!

    private val viewModel: AllSectionsViewModel by viewModels()
    private val uiStateViewModel: UiStateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAllSectionsBinding.inflate(
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

        val adapter = HeadlineAdapter()
        binding.sectionsContentRecyclerview.adapter = adapter

        collectLatestLifecycleFlow(viewModel.sectionsListState) {
            when (it.status) {
                Status.LOADING -> {
                    binding.sectionsContentRecyclerview.visibility = View.GONE
                    binding.shimmerLayout.apply {
                        visibility = View.VISIBLE
                        startShimmerAnimation()
                    }
                }
                Status.SUCCESS -> {
                    binding.shimmerLayout.apply {
                        visibility = View.GONE
                        stopShimmerAnimation()
                    }
                    binding.sectionsContentRecyclerview.visibility = View.VISIBLE
                    it.data?.let { sections ->
                        adapter.submitList(sections.toAllSectionsItem())
                    }
                }
                Status.ERROR -> {
                    it.message?.let { message ->
                        binding.root.snackbar(NETWORK_ERROR_MESSAGE)
                        Log.e(TAG, "Error: $message")
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