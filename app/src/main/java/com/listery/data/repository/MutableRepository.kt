package com.listery.data.repository

import com.listery.data.observer.DataObservable
import com.listery.data.repository.impl.DataStatus


interface MutableRepository {
    val onDataChanged: DataObservable<DataStatus>
}