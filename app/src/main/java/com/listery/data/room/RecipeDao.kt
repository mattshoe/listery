package com.listery.data.room

import androidx.room.*
import com.listery.data.model.UserRecipe
import com.listery.data.room.entities.MeasurementUnitEntity
import com.listery.data.room.entities.recipe.IngredientEntity
import com.listery.data.room.entities.recipe.RecipeEntity
import com.listery.data.room.entities.recipe.RecipeIngredientEntity
import com.listery.data.room.entities.recipe.RecipeIngredientRelationEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class RecipeDao {


    @Query("SELECT EXISTS (SELECT 1 FROM recipe WHERE name == :recipeName)")
    abstract fun recipeExists(recipeName: String): Single<Boolean>

    @Query("SELECT * FROM recipe ORDER BY name ASC")
    abstract fun getAllRecipeEntities(): Single<List<RecipeEntity>>

    @Query("SELECT * FROM recipe WHERE name LIKE :recipeName LIMIT 1")
    abstract fun getRecipeEntity(recipeName: String): Single<RecipeEntity>




    @Query("SELECT EXISTS (SELECT 1 FROM ingredient WHERE name == :ingredientName)")
    abstract fun ingredientExists(ingredientName: String): Single<Boolean>

    @Query("SELECT * FROM ingredient ORDER BY name ASC")
    abstract fun getAllIngredientEntities(): Single<List<IngredientEntity>>

    @Query("SELECT * FROM ingredient WHERE name LIKE :ingredientName LIMIT 1")
    abstract fun getIngredientEntity(ingredientName: String): Single<IngredientEntity>

    @Query("""
        SELECT 
            i.*
        FROM
            ingredient i
            INNER JOIN recipe r
            INNER JOIN recipe_ingredient ri
        ON
            r.name == :recipeName
            AND i.name == ri.ingredientName
            AND r.name == ri.recipeName
        ORDER BY 
            i.name ASC
    """)
    abstract fun getIngredientEntities(recipeName: String): Single<List<IngredientEntity>>

    @Query("SELECT * FROM ingredient WHERE name IN (:ingredientsList)")
    abstract fun getIngredientEntities(ingredientsList: List<String>): Single<List<IngredientEntity>>




    @Query("SELECT * FROM recipe_ingredient WHERE recipeName == :recipeName")
    abstract fun getRecipeIngredients(recipeName: String): Single<List<RecipeIngredientEntity>>

    @Query("SELECT * FROM measurement_unit WHERE name LIKE :name LIMIT 1")
    abstract fun getMeasurementUnit(name: String): Single<MeasurementUnitEntity>



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertIngredientEntities(vararg ingredientEntity: IngredientEntity): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRecipeEntities(vararg recipeEntity: RecipeEntity): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertMeasurementUnit(vararg measurementUnitEntity: MeasurementUnitEntity): Single<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRecipeIngredientEntities(vararg recipeIngredientEntities: RecipeIngredientEntity): Single<List<Long>>

    @Transaction
    open fun insertRecipeAndIngredients(userRecipe: UserRecipe) {
        insertRecipeEntities(userRecipe.entity)
        insertIngredientEntities(*userRecipe.userIngredients.map { it.entity }.toTypedArray())

        userRecipe.userIngredients.map {
            RecipeIngredientEntity(userRecipe.entity.name, it)
        }.also { recipeIngredients ->
            recipeIngredients.firstOrNull()?.let {
                insertRecipeIngredientEntities(*recipeIngredients.toTypedArray())
            }
        }
    }
}