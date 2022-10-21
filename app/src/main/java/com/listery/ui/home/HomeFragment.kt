package com.listery.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.listery.R
import com.listery.databinding.FragmentHomeBinding
import com.listery.di.ApplicationComponent
import com.listery.di.get
import com.listery.ui.BaseFragment
import com.listery.ui.NoArgs
import javax.inject.Inject
import javax.inject.Provider

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding, NoArgs>() {
    override val viewModelClass = HomeViewModel::class.java

    @Inject
    lateinit var adapterProvider: Provider<ShoppingListAdapter>

    override fun inject(component: ApplicationComponent) = component.inject(this)
    override fun bind(i: LayoutInflater, c: ViewGroup?, a: Boolean) = FragmentHomeBinding.inflate(i, c, a)

    override fun configureToolbar(toolbar: Toolbar) {
        super.configureToolbar(toolbar)
        viewModel.shoppingLists.observe(viewLifecycleOwner) { lists ->
            toolbar.findViewById<Spinner>(R.id.list_spinner)?.let { spinner ->
                spinner.adapter = ArrayAdapter(
                    this@HomeFragment.requireContext(),
                    R.layout.shopping_list_spinner_item,
                    R.id.spinner_text,
                    lists.map { it.entity.name }
                )
            }
        }
        toolbar.rootView.findViewById<Spinner>(R.id.list_spinner).apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) { }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.loadList(this@apply?.selectedItem as? String ?: "Shopping")
                }
            }
        }
    }

    override fun onCreateView(
        savedInstanceState: Bundle?
    ) {
        viewModel.loadData()
        viewModel.selectedList.observe(
            viewLifecycleOwner,
            Observer { list ->
                binding.recipeList.apply {
                    Log.d("MATTSHOE", list.entity.name)
                    layoutManager = LinearLayoutManager(
                        view?.context,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                    adapter = adapterProvider.get(list.items)
                }
            }
        )
    }

}