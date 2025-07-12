package org.mattshoe.shoebox.listery.cuisine.data

import kotlinx.coroutines.flow.StateFlow
import org.mattshoe.shoebox.listery.cuisine.model.Cuisine

interface CuisineRepository {
    val data: StateFlow<List<Cuisine>>
}

class CuisineFirestoreModel(
    val name: String = "",
    val description: String = ""
)

fun CuisineFirestoreModel.toCuisine(): Cuisine {
    return Cuisine(name, description)
}