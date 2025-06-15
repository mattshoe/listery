package org.mattshoe.shoebox.listery.model

import androidx.annotation.DrawableRes

interface GenericErrorScreenState {
    @get:DrawableRes
    val icon: Int
    val message: String
}