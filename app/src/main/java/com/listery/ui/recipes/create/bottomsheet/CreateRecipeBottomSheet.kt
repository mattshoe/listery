package com.listery.ui.recipes.create.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.listery.data.repository.impl.RecipeRepository
import com.listery.databinding.CreateRecipeBottomSheetBinding
import com.listery.di.ApplicationComponent
import com.listery.ui.NoArgs
import com.listery.ui.dialog.BottomSheet
import com.listery.ui.recipes.home.RecipesHomeFragmentDirections
import javax.inject.Inject

class CreateRecipeBottomSheet: BottomSheet<CreateRecipeBottomSheetViewModel, CreateRecipeBottomSheetBinding, NoArgs>() {
    override val viewModelClass = CreateRecipeBottomSheetViewModel::class.java

    override fun bind(i: LayoutInflater, c: ViewGroup?, a: Boolean) = CreateRecipeBottomSheetBinding.inflate(i, c, a)
    override fun inject(component: ApplicationComponent) = component.inject(this)

    override fun onCreateView(savedInstanceState: Bundle?) {
        super.onCreateView(savedInstanceState)

        binding.recipeNameInput.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                binding.submitRecipeButton.isEnabled = binding.recipeNameInput.text.isNotEmpty()
            }
        })

        binding.submitRecipeButton.setOnClickListener {
            binding.recipeNameInput.isEnabled = false
            binding.recipeNotesInput.isEnabled = false

            viewModel.addRecipe(
                binding.recipeNameInput.text.toString(),
                binding.recipeNotesInput.text.toString()
            )
        }

        viewModel.recipeCreatedSuccessfully.observe {
            findNavController().navigate(
                CreateRecipeBottomSheetDirections.actionCreateRecipeBottomSheetToNavigationRecipeCreate(
                    binding.recipeNameInput.text.toString()
                )
            )

            dismiss()
        }
    }


}