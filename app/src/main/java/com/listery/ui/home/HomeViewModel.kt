package com.listery.ui.home

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.listery.data.model.UserShoppingList
import com.listery.data.repository.IShoppingListRepository
import com.listery.ui.BaseViewModel
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    application: Application,
    private val shoppingListRepository: IShoppingListRepository
) : BaseViewModel(application) {

    val shoppingLists: MutableLiveData<List<UserShoppingList>> = MutableLiveData()

    init {
        addDisposable(
            shoppingListRepository.getAllShoppingLists()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        shoppingLists.postValue(it)
                    },
                    {}
                )
        )
    }

}