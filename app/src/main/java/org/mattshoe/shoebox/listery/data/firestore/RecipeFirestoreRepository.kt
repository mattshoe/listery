package org.mattshoe.shoebox.listery.data.firestore

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
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
        sessionRepository.session
            .filterIsInstance<SessionState.LoggedIn>()
            .flatMapLatest { session ->
                observeRecipeUpdates(session.user.id)
            }
            .onEach { recipes->
                _recipes.update {
                    recipes
                }
            }.launchIn(CoroutineScope(Dispatchers.IO))
    }

    override suspend fun fetch(id: String): Recipe? {
        return _recipes.value.firstOrNull {
            it.id == id
        }
    }

    override suspend fun upsert(recipe: Recipe) {
        sessionRepository.currentUser?.id?.let { userId ->
            val recipeId = if (recipe.id.isBlank()) UUID.randomUUID().toString() else recipe.id
            firestore
                .document("users/${userId}/recipes/$recipeId")
                .set(recipe.toFirestoreModel())
                .await()
        }
    }

    override suspend fun remove(id: String) {
        sessionRepository.currentUser?.id?.let { userId ->
            firestore
                .document("users/${userId}/recipes/$id")
                .delete()
                .await()
        }
    }

    override suspend fun exists(name: String): Boolean {
        return _recipes.value.any { it.name == name }
    }

    override fun observe(name: String): Flow<Recipe?> {
        return _recipes.map { recipes ->
            recipes.firstOrNull { it.name == name }
        }
    }

    private fun observeRecipeUpdates(userId: String): Flow<List<Recipe>> =
        callbackFlow<List<Recipe>> {
            val listener = firestore.collection("users/$userId/recipes")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    val recipes = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(FirestoreRecipeModel::class.java)
                            ?.copy(id = doc.id)
                            ?.toRecipe()
                    } ?: emptyList()

                    trySend(recipes)
                }

            awaitClose {
                listener.remove()
            }
        }
}