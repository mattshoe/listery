package com.listery.ui.home

import android.app.Application
import com.listery.ui.BaseViewModel
import com.listery.ui.NoArgs
import javax.inject.Inject

class ShoppingListViewModel @Inject constructor(
    application: Application
): BaseViewModel<NoArgs>(application) {
}