package com.listery.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavArgs
import androidx.viewbinding.ViewBinding
import com.listery.MainActivity
import com.listery.di.ApplicationComponent
import com.listery.di.ListeryInjector
import com.listery.di.ViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

typealias NoArgs = NavArgs

abstract class BaseFragment<TViewModel: BaseViewModel<TArgs>, TBinding: ViewBinding, TArgs: NavArgs>: Fragment(),
    DefaultLifecycleObserver {
    private lateinit var _binding: TBinding
    private lateinit var _viewModel: TViewModel
    private val observers = mutableSetOf<LiveData<*>>()

    protected abstract val viewModelClass: Class<TViewModel>
    protected val viewModel get() = _viewModel
    protected val binding get() = _binding
    protected val toolbar: Toolbar
        get() = (requireActivity() as MainActivity).toolbar

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected abstract fun inject(component: ApplicationComponent)
    protected abstract fun bind(i: LayoutInflater, c: ViewGroup?, a: Boolean): TBinding
    protected open fun buildNavArgs(bundle: Bundle): TArgs? = null

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().lifecycle.addObserver(this)
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super<Fragment>.onCreate(savedInstanceState)
        inject(ListeryInjector.build(requireActivity()))

        _viewModel = ViewModelProvider(this, viewModelFactory)[viewModelClass].apply {
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
        _binding = bind(inflater, container, false)
        return binding.root.also {
            onCreateView(savedInstanceState)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        unregisterObservers()
    }

    override fun onCreate(owner: LifecycleOwner) {
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = null
        configureToolbar(toolbar)
    }

    open fun onCreateView(savedInstanceState: Bundle?) { }
    open fun configureToolbar(toolbar: Toolbar) { }

    protected fun <T> observe(liveData: LiveData<T>, observer: (T) -> Unit) {
        registerObserver(liveData)
        liveData.observe(viewLifecycleOwner) {
            observer.invoke(it)
        }
    }

    private fun registerObserver(liveData: LiveData<*>) {
        if (observers.none { it === liveData }) {
            observers.add(liveData)
        }
    }

    private fun unregisterObservers() {
        observers.forEach {
            it.removeObservers(viewLifecycleOwner)
        }
    }

}