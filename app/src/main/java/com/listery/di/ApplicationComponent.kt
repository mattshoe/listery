package com.listery.di

import com.listery.ListeryApplication
import com.listery.MainActivity
import com.listery.data.repository.di.RepositoryModule
import com.listery.data.room.di.RoomModule
import com.listery.ui.home.HomeFragment
import com.listery.ui.profile.ProfileFragment
import com.listery.ui.recipes.create.RecipeCreateFragment
import com.listery.ui.recipes.create.bottomsheet.AddIngredientBottomSheet
import com.listery.ui.recipes.create.bottomsheet.CreateRecipeBottomSheet
import com.listery.ui.recipes.home.RecipesHomeFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton


// Definition of the Application graph
@Component(
    modules = [
        AppModule::class,
        RepositoryModule::class,
        RoomModule::class,
        ViewModelModule::class
    ]
)
@Singleton
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: HomeFragment)

    fun inject(fragment: RecipesHomeFragment)

    fun inject(fragment: RecipeCreateFragment)

    fun inject(fragment: ProfileFragment)

    fun inject(dialog: CreateRecipeBottomSheet)

    fun inject(dialog: AddIngredientBottomSheet)

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent

        @BindsInstance
        fun application(application: ListeryApplication): Builder
    }

}

