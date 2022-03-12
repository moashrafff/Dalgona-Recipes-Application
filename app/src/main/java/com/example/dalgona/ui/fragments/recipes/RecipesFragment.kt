package com.example.dalgona.ui.fragments.recipes

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.dalgona.R
import com.example.dalgona.adapters.RecipesAdapter
import com.example.dalgona.databinding.FragmentRecipesBinding
import com.example.dalgona.utils.NetworkListener
import com.example.dalgona.utils.NetworkResult
import com.example.dalgona.utils.observeOnce
import com.example.dalgona.viewmodels.MainViewModel
import com.example.dalgona.viewmodels.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RecipesFragment : Fragment(), SearchView.OnQueryTextListener  {

    private lateinit var _binding: FragmentRecipesBinding
    private val binding get() = _binding
    private val mAdapter by lazy { RecipesAdapter() }
    private val mainViewModel: MainViewModel by viewModels()
    private val recipesViewModel: RecipesViewModel by viewModels()
    private val args by navArgs<RecipesFragmentArgs>()
    private lateinit var networkListener: NetworkListener



    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesBinding.inflate(layoutInflater)

//necessary for data binding
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel


        //setting up the adapter
        binding.shimmerRv.adapter = mAdapter


        //setting up the searchBar
        setHasOptionsMenu(true)


        //network listener class
        recipesViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipesViewModel.backOnline = it
        })

        lifecycleScope.launchWhenCreated {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect {
                    status ->
                recipesViewModel.networkStatus = status
                recipesViewModel.showNetworkStatus()
                readDatabase()
            }
        }


        //to hide the fab on scroll
        binding.shimmerRv.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            Timber.i(v.toString())
            Timber.i(scrollX.toString())
            Timber.i(scrollY.toString())
            Timber.i(oldScrollX.toString())
            Timber.i(oldScrollY.toString())

            if (oldScrollY < 0) {
                binding.recipesFab.hide()
            } else {
                binding.recipesFab.show()
            }
        }

        binding.recipesFab.setOnClickListener {
            if (recipesViewModel.networkStatus) {
                val directions =
                    RecipesFragmentDirections.actionRecipesFragmentToRecipesBottomSheet()
                findNavController().navigate(directions)
            } else {
                recipesViewModel.showNetworkStatus()
            }
        }

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showShimmerEffect()

        readDatabase()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.recipes_menu,menu)


        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    Timber.i("readDatabase called!")
                    mAdapter.setData(database[0].foodRecipe)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }
            })
        }
    }

    private fun requestApiData() {

        Timber.i("api call")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())

        mainViewModel.apiRecipes.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }


    private fun searchApiData(searchQuery : String){
        showShimmerEffect()
        mainViewModel.searchRecipes(recipesViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchRecipes.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }

    private fun showShimmerEffect() {
        binding.shimmerRv.showShimmer()
    }

    private fun hideShimmerEffect() {
        binding.shimmerRv.hideShimmer()
    }


}