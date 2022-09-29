package com.listery.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.listery.di.ListeryInjector
import com.listery.di.ApplicationComponent
import com.listery.di.ViewModelFactory
import javax.inject.Inject

abstract class BaseFragment<TViewModel: BaseViewModel, TBinding: ViewBinding>: Fragment() {
    private lateinit var _binding: TBinding
    private lateinit var _viewModel: TViewModel

    protected abstract val viewModelClass: Class<TViewModel>
    protected val viewModel get() = _viewModel
    protected val binding get() = _binding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected abstract fun inject(component: ApplicationComponent)
    protected abstract fun bind(i: LayoutInflater, c: ViewGroup?, a: Boolean): TBinding

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(ListeryInjector.build(requireActivity()))
        _viewModel = ViewModelProvider(this, viewModelFactory)[viewModelClass]
    }

    @CallSuper
    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = bind(inflater, container, false)
        return binding.root.also {
            onCreateView(savedInstanceState)
        }
    }

    open fun onCreateView(savedInstanceState: Bundle?) { }

}