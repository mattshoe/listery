package com.listery.ui.toolbar

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import com.listery.R

enum class ToolbarContext(@IdRes val layoutId: Int) {
    SHOPPTING_LIST(R.id.shopping_list_layout),
    TITLE(R.id.title_layout)
}