package com.listery.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.listery.ui.home.HomeViewModel
import com.listery.ui.recipes.RecipesViewModel
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
    @ViewModelKey(RecipesViewModel::class)
    fun recipesViewModel(viewModel: RecipesViewModel): ViewModel


    fun homeViewModel(): HomeViewModel
    fun recipesViewModel(): RecipesViewModel
}




@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)