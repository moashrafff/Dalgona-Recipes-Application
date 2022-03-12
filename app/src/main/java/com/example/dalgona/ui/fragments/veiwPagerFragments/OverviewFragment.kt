package com.example.dalgona.ui.fragments.veiwPagerFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import coil.load
import com.example.dalgona.R
import com.example.dalgona.adapters.bindingadapters.RecipesRowBinding
import com.example.dalgona.databinding.FragmentOverviewBinding
import com.example.dalgona.data.network.models.Result
import com.example.dalgona.utils.Constants.RECIPE_RESULT_KEY


class OverviewFragment : Fragment() {

    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        if (myBundle != null) {
            binding.mainImageView.load(myBundle.image)

            binding.titleTextView.text = myBundle.title
            binding.likesTextView.text = myBundle.aggregateLikes.toString()
            binding.timeTextView.text = myBundle.readyInMinutes.toString()

            //the summary contains text of html tags, to remove them Jsoup library is used.
            RecipesRowBinding.parseHtml(binding.summaryTextView, myBundle.summary)

            updateColors(
                myBundle.vegetarian,
                binding.vegetarianTextView,
                binding.vegetarianImageView
            )
            updateColors(myBundle.vegan, binding.veganTextView, binding.veganImageView)
            updateColors(myBundle.cheap, binding.cheapTextView, binding.cheapImageView)
            updateColors(myBundle.dairyFree, binding.dairyFreeTextView, binding.dairyFreeImageView)
            updateColors(
                myBundle.glutenFree,
                binding.glutenFreeTextView,
                binding.glutenFreeImageView
            )
            updateColors(myBundle.veryHealthy, binding.healthyTextView, binding.healthyImageView)
        }else{
            Log.i("uni", "error in overview")
        }
        return binding.root

    }
    private fun updateColors(stateIsOn: Boolean, textView: TextView, imageView: ImageView) {
        if (stateIsOn) {
            imageView.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}