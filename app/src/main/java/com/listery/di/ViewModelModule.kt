package com.listery.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.listery.ui.home.HomeViewModel
import com.listery.ui.recipes.create.RecipeCreateViewModel
import com.listery.ui.recipes.create.bottomsheet.AddIngredientBottomSheetViewModel
import com.listery.ui.recipes.create.bottomsheet.CreateRecipeBottomSheetViewModel
import com.listery.ui.recipes.home.RecipeHomeViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    fun homeViewModel(viewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipeHomeViewModel::class)
    fun recipeHomeViewModel(viewModel: RecipeHomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RecipeCreateViewModel::class)
    fun recipeCreateViewModel(viewModel: RecipeCreateViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateRecipeBottomSheetViewModel::class)
    fun createRecipeBottomSheetViewModel(viewModel: CreateRecipeBottomSheetViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddIngredientBottomSheetViewModel::class)
    fun addIngredientBottomSheetViewModel(viewModel: AddIngredientBottomSheetViewModel): ViewModel


    fun homeViewModel(): HomeViewModel
    fun recipeHomeViewModel(): RecipeHomeViewModel
    fun recipeCreateViewModel(): RecipeCreateViewModel
    fun createRecipeBottomSheetViewModel(): CreateRecipeBottomSheetViewModel
    fun addIngredientBottomSheetViewModel(): AddIngredientBottomSheetViewModel
}




@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)