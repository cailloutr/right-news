package com.cailloutr.rightnews.ui.newsdetailsfragment

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.cailloutr.rightnews.databinding.FragmentNewsDetailsBinding
import com.cailloutr.rightnews.extensions.setupPaddingInView
import com.cailloutr.rightnews.ui.viewmodel.UiStateViewModel
import com.cailloutr.rightnews.ui.viewmodel.VisualComponents
import com.cailloutr.rightnews.util.DateUtil

//private const val TAG = "NewsDetailsFragment"

//TODO: create placeholders for empty view like text and images
class NewsDetailsFragment : Fragment() {

    private var _binding: FragmentNewsDetailsBinding? = null
    val binding get() = _binding!!

    private val args: NewsDetailsFragmentArgs? by navArgs()

    private val uiStateViewModel: UiStateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentNewsDetailsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiStateViewModel.hasComponents = VisualComponents(bottomNavigation = false)

        val article = args?.article

        article?.let {
            binding.apply {
                image.load(it.thumbnail)
                publicationDate.text = DateUtil.getFormattedDate(it.webPublicationDate)
                headline.text = it.webTitle
                content.text = Html.fromHtml(it.body, Html.FROM_HTML_MODE_COMPACT) ?: ""
            }
        }


        binding.content.setupPaddingInView(
            binding.fragmentNewsDetailsContainer,
            binding.fragmentNewsDetailsCard
        )


        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addFavorite.setOnClickListener {
            //TODO()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}