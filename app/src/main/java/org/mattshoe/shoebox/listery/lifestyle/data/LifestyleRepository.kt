package org.mattshoe.shoebox.listery.lifestyle.data

import kotlinx.coroutines.flow.StateFlow
import org.mattshoe.shoebox.listery.cuisine.model.Cuisine
import org.mattshoe.shoebox.listery.lifestyle.model.Lifestyle

interface LifestyleRepository {
    val data: StateFlow<List<Lifestyle>>
}

class LifestyleFirestoreModel(
    val name: String = "",
    val description: String = ""
)

fun LifestyleFirestoreModel.toLifestyle(): Lifestyle {
    return Lifestyle(name, description)
}