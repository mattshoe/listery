package org.mattshoe.shoebox.listery.data.firestore

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.tasks.await
import org.mattshoe.shoebox.listery.authentication.data.SessionRepository
import org.mattshoe.shoebox.listery.authentication.model.SessionState
import org.mattshoe.shoebox.listery.data.RecipeRepository
import org.mattshoe.shoebox.listery.data.util.asFlow
import org.mattshoe.shoebox.listery.model.Recipe
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeFirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val sessionRepository: SessionRepository
): RecipeRepository {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    override val userRecipes: Flow<List<Recipe>> = _recipes.asStateFlow()

    init {
        sessionRepository.state
            .filterIsInstance<SessionState.LoggedIn>()
            .flatMapLatest { session ->
                observeRecipeUpdates(session.user.id)
                    .combine(
                        observeIngredientUpdates(session.user.id)
                    ) { recipes, ingredients ->
                        val ingredientsMap = ingredients.associateBy { it.id }
                        recipes.map {
                            it.toRecipe(ingredientsMap)
                        }
                    }
            }
            .onEach { recipes ->
                _recipes.update { recipes }
            }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    override suspend fun fetch(id: String): Recipe? {
        return _recipes.value.firstOrNull {
            it.id == id
        }
    }

    override suspend fun upsert(recipe: Recipe): String {
        val recipeId = if (recipe.id.isBlank()) UUID.randomUUID().toString() else recipe.id
        sessionRepository.currentUser?.id?.let { userId ->
            buildList {
                add(
                    firestore
                        .document("users/${userId}/recipes/$recipeId")
                        .set(recipe.toFirestoreModel())
                )
                recipe.ingredients.forEach {
                    add(
                        firestore
                            .document("users/$userId/ingredients/${it.id}")
                            .set(it.toFirestoreModel())
                    )
                }
            }.forEach {
                it.await()
            }
        }
        return recipeId
    }

    override suspend fun remove(id: String) {
        sessionRepository.currentUser?.id?.let { userId ->
            firestore
                .document("users/${userId}/recipes/$id")
                .delete()
                .await()
        }
    }

    override suspend fun exists(id: String): Boolean {
        return _recipes.value.any { it.name == id }
    }

    override fun observe(id: String): Flow<Recipe?> {
        return _recipes.map { recipes ->
            recipes.firstOrNull { it.id == id }
        }
    }

    private fun observeRecipeUpdates(userId: String): Flow<List<FirestoreRecipeModel>> =
        firestore
            .collection("users/$userId/recipes")
            .asFlow()

    private fun observeIngredientUpdates(userId: String): Flow<List<IngredientFirestoreModel>> =
        firestore
            .collection("users/$userId/ingredients")
            .asFlow()

}