package org.mattshoe.shoebox.listery.lifestyle.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.mattshoe.shoebox.listery.cuisine.model.Cuisine
import org.mattshoe.shoebox.listery.data.util.asFlow
import org.mattshoe.shoebox.listery.lifestyle.model.Lifestyle
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
class LifestyleRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): LifestyleRepository {
    private val _data = MutableStateFlow<List<Lifestyle>>(emptyList())
    override val data: StateFlow<List<Lifestyle>> = _data.asStateFlow()

    init {
        firestore.collection("lifestyles")
            .asFlow<LifestyleFirestoreModel>()
            .onEach { cuisines ->
                _data.update {
                    cuisines.map {
                        it.toLifestyle()
                    }.filter {
                        it.name.isNotBlank()
                    }
                }
            }
            .flowOn(Dispatchers.IO)
            .launchIn(GlobalScope)
    }
}