package com.listery.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.listery.di.ApplicationComponent
import com.listery.di.ListeryInjector
import com.listery.di.ViewModelFactory
import com.listery.ui.BaseViewModel
import javax.inject.Inject

abstract class BottomSheet<TViewModel: BaseViewModel<TArgs>, TBinding: ViewBinding, TArgs: NavArgs> : BottomSheetDialogFragment() {
    protected lateinit var viewModel: TViewModel
        private set

    protected abstract val viewModelClass: Class<TViewModel>

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected lateinit var binding: TBinding
        private set

    protected abstract fun inject(component: ApplicationComponent)
    protected abstract fun bind(i: LayoutInflater, c: ViewGroup?, a: Boolean): TBinding
    protected open fun buildNavArgs(bundle: Bundle): TArgs? = null

    open fun onCreateView(savedInstanceState: Bundle?) { }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject(ListeryInjector.build(requireActivity()))

        viewModel = ViewModelProvider(this, viewModelFactory)[viewModelClass].apply {
            arguments?.let { bundle ->
                buildNavArgs(bundle)?.let { args ->
                    setArguments(args)
                }
            }
        }
    }

    @CallSuper
    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = bind(inflater, container, false)
        return binding.root.also {
            onCreateView(savedInstanceState)
        }
    }
}