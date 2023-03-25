package com.listery.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.listery.ListeryApplication
import com.listery.MainActivity
import com.listery.R
import com.listery.di.ApplicationComponent
import com.listery.di.ViewModelFactory
import com.listery.ui.toolbar.ToolbarContext
import javax.inject.Inject

typealias NoArgs = NavArgs

abstract class BaseFragment<TViewModel: BaseViewModel<TArgs>, TBinding: ViewBinding, TArgs: NavArgs>: Fragment(),
    DefaultLifecycleObserver {
    private lateinit var _binding: TBinding
    private lateinit var _viewModel: TViewModel
    private val observers = mutableSetOf<LiveData<*>>()
    private val toolbar: Toolbar get() = (requireActivity() as MainActivity).toolbar

    protected abstract val viewModelClass: Class<TViewModel>
    protected abstract val toolbarContext: ToolbarContext

    protected open val title: CharSequence? = null
    protected val viewModel get() = _viewModel
    protected val binding get() = _binding

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
        inject(ListeryApplication.applicationComponent)

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
    }

    override fun onResume() {
        super<Fragment>.onResume()
        initializeToolbar()
    }

    open fun onCreateView(savedInstanceState: Bundle?) { }
    open fun configureToolbar(toolbar: Toolbar, layout: ViewGroup) { }

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

    private fun initializeToolbar() {
        toolbar.title = null
        ToolbarContext.values().forEach {
            toolbar.findViewById<ViewGroup>(it.layoutId).visibility =
                if (it == toolbarContext)
                    View.VISIBLE
                else
                    View.GONE
        }
        val layout: ViewGroup = toolbar.findViewById(toolbarContext.layoutId)
        if (toolbarContext == ToolbarContext.TITLE) {
            layout.findViewById<TextView>(R.id.toolbar_title_text).text = title
        } else {
            configureToolbar(toolbar, layout)
        }
    }

}