package com.listery.ui.recipes.create

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.listery.databinding.FragmentRecipeCreateBinding
import com.listery.di.ApplicationComponent
import com.listery.di.get
import com.listery.ui.BaseFragment
import com.listery.ui.recipes.create.recyclerview.IngredientListAdapter
import javax.inject.Inject
import javax.inject.Provider

class RecipeCreateFragment: BaseFragment<RecipeCreateViewModel, FragmentRecipeCreateBinding, RecipeCreateFragmentArgs>() {
    override val viewModelClass = RecipeCreateViewModel::class.java

    @Inject
    lateinit var adapterProvider: Provider<IngredientListAdapter>

    override fun inject(component: ApplicationComponent) = component.inject(this)
    override fun bind(i: LayoutInflater, c: ViewGroup?, a: Boolean) = FragmentRecipeCreateBinding.inflate(i, c, a)
    override fun buildNavArgs(bundle: Bundle): RecipeCreateFragmentArgs = RecipeCreateFragmentArgs.fromBundle(bundle)

    override fun onCreateView(savedInstanceState: Bundle?) {
        viewModel.recipe.observe(viewLifecycleOwner) {
            binding.recipeNameInput.text = it.entity.name
            binding.recipeNotesInput.setText(it.entity.notes)
            binding.ingredientsList.apply {
                layoutManager = LinearLayoutManager(
                    view?.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = adapterProvider.get(it.userIngredients)

                if (it.userIngredients.isEmpty())
                    binding.ingredientsEmptyMessage.visibility = View.VISIBLE
                else
                    binding.ingredientsEmptyMessage.visibility = View.GONE
            }
        }

        binding.addIngredientButton.setOnClickListener {
            findNavController().navigate(
                RecipeCreateFragmentDirections.actionNavigationRecipeCreateToAddIngredientBottomSheet(
                    viewModel.recipe.value.toString()
                )
            )
        }
    }
}