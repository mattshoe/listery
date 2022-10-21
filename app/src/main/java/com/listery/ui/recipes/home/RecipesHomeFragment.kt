package com.listery.ui.recipes.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.listery.databinding.FragmentRecipeHomeBinding
import com.listery.di.ApplicationComponent
import com.listery.di.get
import com.listery.ui.BaseFragment
import com.listery.ui.NoArgs
import com.listery.ui.recipes.home.recyclerview.RecipeListAdapter
import javax.inject.Inject
import javax.inject.Provider

class RecipesHomeFragment: BaseFragment<RecipeHomeViewModel, FragmentRecipeHomeBinding, NoArgs>() {
    override val viewModelClass = RecipeHomeViewModel::class.java

    @Inject
    lateinit var adapterProvider: Provider<RecipeListAdapter>

    override fun inject(component: ApplicationComponent) = component.inject(this)
    override fun bind(i: LayoutInflater, c: ViewGroup?, a: Boolean) = FragmentRecipeHomeBinding.inflate(i, c, a)

    override fun onCreateView(savedInstanceState: Bundle?) {
        observe(viewModel.recipes) {recipes ->
            binding.recipeList.apply {
                layoutManager = LinearLayoutManager(
                    view?.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = adapterProvider.get(recipes)
            }
        }
        viewModel.loadData()

        binding.addRecipeButton.setOnClickListener {
            findNavController().navigate(
                RecipesHomeFragmentDirections.actionNavigationRecipesHomeToNavigationRecipeCreate(null)
            )
        }
    }
}