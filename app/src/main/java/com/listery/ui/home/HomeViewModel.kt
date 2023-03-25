package com.listery.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.listery.applyIOScheduler
import com.listery.data.model.UserShoppingList
import com.listery.data.repository.IShoppingListRepository
import com.listery.ui.BaseViewModel
import com.listery.ui.NoArgs
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    application: Application,
    private val shoppingListRepository: IShoppingListRepository
) : BaseViewModel<NoArgs>(application) {

    val shoppingLists: MutableLiveData<List<UserShoppingList>> = MutableLiveData()
    val selectedList: MutableLiveData<UserShoppingList> = MutableLiveData()

    init {
        addDisposable(
            shoppingListRepository.onDataChanged.subscribe {
                loadData()
            }
        )
    }

    fun loadList(listName: String) {
        if (listName != selectedList.value?.entity?.name)
            selectedList.postValue(shoppingLists.value?.first { it.entity.name == listName })
    }

    fun loadData(listName: String? = null) {
        addDisposable(
            shoppingListRepository.getAllShoppingLists()
                .applyIOScheduler()
                .subscribe(
                    {
                        Log.d("MATTSHOE", "loading")
                        shoppingLists.postValue(it)
                        selectedList.postValue(it.first())
                    },
                    {}
                )
        )
    }

}