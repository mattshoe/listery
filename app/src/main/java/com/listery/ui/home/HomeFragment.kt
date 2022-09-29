package com.listery.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.listery.databinding.FragmentHomeBinding
import com.listery.di.ApplicationComponent
import com.listery.di.get
import com.listery.ui.BaseFragment
import javax.inject.Inject
import javax.inject.Provider

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>() {
    override val viewModelClass = HomeViewModel::class.java

    @Inject
    lateinit var adapterProvider: Provider<RecipeListAdapter>

    override fun inject(component: ApplicationComponent) = component.inject(this)
    override fun bind(i: LayoutInflater, c: ViewGroup?, a: Boolean) = FragmentHomeBinding.inflate(i, c, a)

    override fun onCreateView(
        savedInstanceState: Bundle?
    ) {
        viewModel.recipes.observe(
            viewLifecycleOwner,
            Observer { recipes ->
                binding.recipeList.apply {
                    layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)
                    adapter = adapterProvider.get(recipes)
                }
            }
        )
    }

}