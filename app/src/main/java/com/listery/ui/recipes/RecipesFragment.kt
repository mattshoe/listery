package com.listery.ui.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.listery.databinding.FragmentRecipesBinding
import com.listery.di.ApplicationComponent
import com.listery.di.get
import com.listery.ui.BaseFragment
import com.listery.ui.recipes.recyclerview.RecipeListAdapter
import javax.inject.Inject
import javax.inject.Provider

class RecipesFragment: BaseFragment<RecipesViewModel, FragmentRecipesBinding>() {
    override val viewModelClass = RecipesViewModel::class.java

    @Inject
    lateinit var adapterProvider: Provider<RecipeListAdapter>

    override fun inject(component: ApplicationComponent) = component.inject(this)
    override fun bind(i: LayoutInflater, c: ViewGroup?, a: Boolean) = FragmentRecipesBinding.inflate(i, c, a)

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
    }
}