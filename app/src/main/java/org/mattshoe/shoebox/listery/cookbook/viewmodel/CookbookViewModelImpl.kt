package org.mattshoe.shoebox.listery.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mattshoe.shoebox.listery.model.Recipe
import javax.inject.Inject

@HiltViewModel
class CookBookViewModelImpl @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow<CookBookState>(CookBookState.Loading)
    val state: StateFlow<CookBookState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                CookBookState.Success(
                    listOf(
                        Recipe(
                            "Avocado toast and eggs",
                            false,
                            "https://www.reducetarian.com",
                            275,
                            5,
                            "20 min"
                        ),
                        Recipe(
                            "Poop on a stick",
                            false,
                            "https://www.reducetarian.com",
                            275,
                            5,
                            "45 min"
                        ),
                        Recipe(
                            "Kraft mac n cheese",
                            true,
                            "https://www.reducetarian.com",
                            400,
                            2,
                            "20 min"
                        ),
                        Recipe(
                            "Cauliflower Pizza",
                            true,
                            "https://www.reducetarian.com",
                            700,
                            1,
                            "20 min"
                        ),
                        Recipe(
                            "Papa John's",
                            false,
                            "https://www.reducetarian.com",
                            1250,
                            1,
                            "1 hr"
                        ),
                        Recipe(
                            "Butter Chicken",
                            false,
                            "https://www.reducetarian.com",
                            650,
                            24,
                            "1 hr 10 min"
                        ),
                        Recipe(
                            "Sunday WingDay",
                            true,
                            "https://www.reducetarian.com",
                            400,
                            3,
                            "35 min"
                        ),
                        Recipe(
                            "Spaghetti O's",
                            false,
                            "https://www.google.com",
                            200,
                            1,
                            "5 min"
                        ),
                        Recipe(
                            "Fish and Chips",
                            true,
                            "https://www.reducetarian.com",
                            525,
                            8,
                            "45 min"
                        )
                    )
                )
            }
        }
    }
}