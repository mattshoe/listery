package com.listery.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.listery.databinding.FragmentHomeBinding
import com.listery.di.AndroidInjector
import com.listery.di.ViewModelFactory
import com.listery.di.get
import javax.inject.Inject
import javax.inject.Provider

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val dataBinding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var adapterProvider: Provider<RecipeListAdapter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjector.build(requireActivity()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = dataBinding.root

        homeViewModel.recipes.observe(
            viewLifecycleOwner,
            Observer { recipes ->
                dataBinding.recipeList.apply {
                    layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
                    adapter = adapterProvider.get(recipes)
                }
            }
        )

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}