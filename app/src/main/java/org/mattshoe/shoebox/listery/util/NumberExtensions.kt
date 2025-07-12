package org.mattshoe.shoebox.listery.util

import java.util.Locale


fun Number.pretty(): String =
    if (this.toDouble() % 1.0 == 0.0) {
        this.toInt().toString()
    } else {
        String.format(Locale.getDefault(), "%.2f", this.toDouble())
    }