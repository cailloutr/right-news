package com.cailloutr.rightnews.ui.allsectionsfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.cailloutr.rightnews.data.local.roommodel.toSection
import com.cailloutr.rightnews.databinding.FragmentAllSectionsBinding
import com.cailloutr.rightnews.extensions.collectLatestLifecycleFlow
import com.cailloutr.rightnews.extensions.hide
import com.cailloutr.rightnews.extensions.setupToolbar
import com.cailloutr.rightnews.extensions.show
import com.cailloutr.rightnews.model.Section
import com.cailloutr.rightnews.model.Sections
import com.cailloutr.rightnews.model.toAllSectionsItem
import com.cailloutr.rightnews.recyclerview.HeadlineAdapter
import com.cailloutr.rightnews.ui.viewmodel.AllSectionsViewModel
import com.cailloutr.rightnews.ui.viewmodel.UiStateViewModel
import com.cailloutr.rightnews.ui.viewmodel.VisualComponents
import dagger.hilt.android.AndroidEntryPoint


//private const val TAG = "AllSectionsFragment"

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

        val adapter = HeadlineAdapter { section ->
            findNavController().navigate(
                AllSectionsFragmentDirections.actionAllSectionsFragmentToLatestNewsFragment(
                    section.id,
                    section.title
                )
            )
        }

        binding.sectionsContentRecyclerview.adapter = adapter


        collectLatestLifecycleFlow(viewModel.allSectionsState) { roomSectionsList ->
            binding.shimmerLayout.apply {
                hide()
                stopShimmerAnimation()
            }
            binding.sectionsContentRecyclerview.show()

            roomSectionsList.let { sections ->
                val list: List<Section> =
                    roomSectionsList.map { roomSection -> roomSection.toSection() }
                val sectionsList = Sections(
                    total = list.size.toLong(),
                    listOfSections = list
                )
                adapter.submitList(sectionsList.toAllSectionsItem())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}