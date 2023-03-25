package com.listery.data.repository

import com.listery.data.observer.DataObservable
import com.listery.data.repository.impl.DataStatus
import io.reactivex.subjects.PublishSubject


interface MutableRepository {
    val onDataChanged: PublishSubject<DataStatus>
}