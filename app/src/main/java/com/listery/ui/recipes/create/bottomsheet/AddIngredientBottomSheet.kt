package com.listery.ui.recipes.create.bottomsheet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.navigation.fragment.findNavController
import com.listery.data.repository.impl.RecipeRepository
import com.listery.databinding.CreateIngredientBottomSheetBinding
import com.listery.databinding.CreateRecipeBottomSheetBinding
import com.listery.di.ApplicationComponent
import com.listery.ui.dialog.BottomSheet
import com.listery.ui.recipes.home.RecipesHomeFragmentDirections
import javax.inject.Inject

class AddIngredientBottomSheet: BottomSheet<AddIngredientBottomSheetViewModel, CreateIngredientBottomSheetBinding, AddIngredientBottomSheetArgs>() {
    override val viewModelClass = AddIngredientBottomSheetViewModel::class.java

    private val textWatcher = object: TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(text: Editable?) {
            binding.submitIngredientButton.isEnabled = binding.ingredientName.text.isNotEmpty()
                    && binding.quantity.text.isNotEmpty()
                    && binding.unit.text.isNotEmpty()
            Log.d("MATTSHOE", "Enabled = ${binding.submitIngredientButton.isEnabled}")
        }
    }

    override fun bind(i: LayoutInflater, c: ViewGroup?, a: Boolean) = CreateIngredientBottomSheetBinding.inflate(i, c, a)
    override fun buildNavArgs(bundle: Bundle): AddIngredientBottomSheetArgs = AddIngredientBottomSheetArgs.fromBundle(bundle)
    override fun inject(component: ApplicationComponent) {
        component.inject(this)
    }

    override fun onCreateView(savedInstanceState: Bundle?) {
        super.onCreateView(savedInstanceState)

        binding.ingredientName.addTextChangedListener(textWatcher)
        binding.quantity.addTextChangedListener(textWatcher)
        binding.unit.addTextChangedListener(textWatcher)

        viewModel.ingredientAdded.observe(viewLifecycleOwner) {
            dismiss()
        }

        viewModel.ingredientsList.observe(viewLifecycleOwner) {
            binding.ingredientName.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.select_dialog_item,
                    it
                )
            )
        }

        viewModel.unitList.observe(viewLifecycleOwner) {
            binding.unit.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.select_dialog_item,
                    it
                )
            )
        }

        binding.submitIngredientButton.setOnClickListener {
            viewModel.addIngredient(
                binding.ingredientName.text.toString(),
                binding.quantity.text.toString().toDouble(),
                binding.unit.text.toString()
            )
        }

    }


}