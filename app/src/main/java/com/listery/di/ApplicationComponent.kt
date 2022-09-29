package com.listery.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.listery.ListeryApplication
import com.listery.MainActivity
import com.listery.data.di.DataLayerModule
import com.listery.ui.BaseFragment
import com.listery.ui.home.HomeFragment
import com.listery.ui.home.HomeViewModel
import com.listery.ui.profile.ProfileFragment
import com.listery.ui.recipes.RecipesFragment
import com.listery.ui.recipes.RecipesViewModel
import dagger.Binds
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton


// Definition of the Application graph
@Component(
    modules = [
        AppModule::class,
        DataLayerModule::class,
        ViewModelModule::class
    ]
)
@Singleton
interface ApplicationComponent {

    fun inject(activity: MainActivity)

    fun inject(fragment: HomeFragment)

    fun inject(fragment: RecipesFragment)

    fun inject(fragment: ProfileFragment)

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent

        @BindsInstance
        fun application(application: ListeryApplication): Builder
    }

}

@Module
interface AppModule {

    // could also be an abstract class
    @Singleton
    @Binds
    fun bindApplication(application: ListeryApplication): Application

    // if you also want to bind context
    @Singleton
    @Binds
    fun bindContext(application: ListeryApplication): Context
}