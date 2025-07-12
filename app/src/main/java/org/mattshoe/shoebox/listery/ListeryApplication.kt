package org.mattshoe.shoebox.listery

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.mattshoe.shoebox.listery.logging.logi
import org.mattshoe.shoebox.listery.util.ActivityProvider

@HiltAndroidApp
class ListeryApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        // Initialize cuisines and lifestyles in Firestore
        initializeCuisines()
        initializeLifestyles()

        // Register activity lifecycle callbacks to monitor all activities
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                ActivityProvider.currentActivity = activity
            }

            override fun onActivityStarted(activity: Activity) {
                logi("Activity Started: ${activity.javaClass.simpleName}", "ActivityLifecycle")
            }

            override fun onActivityResumed(activity: Activity) {
                logi("Activity Resumed: ${activity.javaClass.simpleName}", "ActivityLifecycle")
            }

            override fun onActivityPaused(activity: Activity) {
                logi("Activity Paused: ${activity.javaClass.simpleName}", "ActivityLifecycle")
            }

            override fun onActivityStopped(activity: Activity) {
                logi("Activity Stopped: ${activity.javaClass.simpleName}", "ActivityLifecycle")
            }

            override fun onActivitySaveInstanceState(
                activity: Activity,
                bundle: Bundle
            ) {
                logi("Activity SaveInstanceState: ${activity.javaClass.simpleName}", "ActivityLifecycle")
            }

            override fun onActivityDestroyed(activity: Activity) {
                ActivityProvider.currentActivity = activity
            }
        })
    }

    private fun initializeCuisines() {
        val cuisines = listOf(
            "American" to "classic comfort foods, burgers, steaks, and traditional dishes",
            "Asian" to "diverse cuisines from across Asia including various regional styles",
            "Australian" to "modern Australian cuisine with multicultural influences",
            "BBQ" to "smoked and grilled meats with rich, smoky flavors",
            "Bakery" to "fresh breads, pastries, and baked goods",
            "Bistro" to "casual French-style dining with simple, quality ingredients",
            "Breakfast" to "morning meals including eggs, pancakes, and breakfast staples",
            "British" to "traditional English and British Isles cuisine",
            "Caribbean" to "tropical flavors with African, European, and indigenous influences",
            "Charcuterie" to "cured meats, pâtés, and artisanal meat preparations",
            "Chinese" to "diverse regional cuisines from China with various cooking techniques",
            "Comfort Food" to "hearty, satisfying dishes that evoke nostalgia and warmth",
            "Creole" to "Louisiana cuisine blending French, Spanish, African, and Native American influences",
            "Cuban" to "Caribbean cuisine with Spanish and African influences",
            "Delicatessen" to "specialty meats, cheeses, and prepared foods",
            "Dessert" to "sweet treats, pastries, and confections",
            "Fermented" to "foods preserved through fermentation processes",
            "French" to "classic French cuisine with refined techniques and rich sauces",
            "Fusion" to "creative combinations of different culinary traditions",
            "Greek" to "Mediterranean cuisine with olive oil, herbs, and fresh ingredients",
            "Grilled" to "foods cooked over direct heat for charred, smoky flavors",
            "Healthy" to "nutritious dishes focused on wellness and balanced nutrition",
            "Hot Pot" to "interactive dining with simmering broth and fresh ingredients",
            "Indian" to "diverse regional cuisines with aromatic spices and complex flavors",
            "Italian" to "traditional Italian cuisine emphasizing fresh ingredients and simplicity",
            "Italian-American" to "American adaptations of Italian dishes and flavors",
            "Japanese" to "refined cuisine emphasizing seasonality, presentation, and umami",
            "Korean" to "bold flavors with fermented ingredients and balanced textures",
            "Mediterranean" to "healthy cuisine from Mediterranean regions with olive oil and fresh produce",
            "Mexican" to "vibrant cuisine with corn, beans, chilies, and fresh herbs",
            "Middle Eastern" to "cuisines from the Middle East with aromatic spices and grains",
            "Pizza" to "Italian flatbread topped with various ingredients and baked",
            "Polish" to "hearty Eastern European cuisine with rich flavors",
            "Ramen" to "Japanese noodle soup with various broths and toppings",
            "Salad" to "fresh vegetable dishes and light, healthy preparations",
            "Seafood" to "dishes featuring fish, shellfish, and other marine ingredients",
            "Soul Food" to "African American cuisine with deep flavors and hearty preparations",
            "Southern" to "American Southern cuisine with rich, comforting flavors",
            "Spanish" to "diverse regional cuisines from Spain with bold flavors",
            "Spicy" to "dishes with significant heat and chili pepper flavors",
            "Sushi" to "Japanese vinegared rice with various ingredients and seafood",
            "Tapas" to "Spanish small plates and appetizers for sharing",
            "Tex-Mex" to "fusion of Texan and Mexican cuisines with bold flavors",
            "Thai" to "balanced cuisine with sweet, sour, salty, and spicy elements",
            "Vietnamese" to "fresh, light cuisine with herbs, rice noodles, and clear broths"
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val firestore = Firebase.firestore
                val cuisinesCollection = firestore.collection("cuisines")
                
                // Check if cuisines already exist
                val existingCuisines = cuisinesCollection.get().await()
                
                if (existingCuisines.isEmpty) {
                    logi("Initializing cuisines collection", "ListeryApplication")
                    
                    // Add all cuisines to Firestore
                    cuisines.forEach { (name, description) ->
                        cuisinesCollection.document(name.lowercase().replace(" ", "-"))
                            .set(mapOf(
                                "name" to name,
                                "description" to description,
                                "createdAt" to com.google.firebase.Timestamp.now()
                            ))
                            .await()
                    }
                    
                    logi("Successfully initialized ${cuisines.size} cuisines", "ListeryApplication")
                } else {
                    logi("Cuisines collection already exists with ${existingCuisines.size()} documents", "ListeryApplication")
                }
            } catch (e: Exception) {
                logi("Error initializing cuisines: ${e.message}", "ListeryApplication")
            }
        }
    }

    private fun initializeLifestyles() {
        val lifestyles = listOf(
            "Vegan" to "excludes all animal products",
            "Vegetarian" to "excludes meat, but may include eggs/dairy",
            "Pescatarian" to "vegetarian + seafood",
            "Flexitarian" to "mostly plant-based with occasional meat",
            "Gluten-Free" to "excludes gluten (wheat, barley, rye)",
            "Dairy-Free / Lactose-Free" to "excludes milk and dairy",
            "Keto (Ketogenic)" to "very low-carb, high-fat",
            "Low-Carb" to "reduced carbohydrate intake",
            "Paleo" to "mimics hunter-gatherer diet (no grains, dairy, legumes)",
            "Whole30" to "30-day clean eating reset (no sugar, grains, alcohol, legumes, etc.)",
            "Raw Food" to "mostly uncooked and unprocessed foods",
            "Organic-Only" to "only consumes certified organic products",
            "Locavore" to "eats locally-sourced food",
            "Intermittent Fasting (IF)" to "eats only during specific time windows",
            "Ayurvedic" to "based on traditional Indian principles (dosha-balancing)",
            "Mediterranean Diet" to "whole grains, fish, olive oil, nuts, minimal red meat",
            "Anti-Inflammatory Diet" to "focuses on foods that reduce inflammation",
            "Low-FODMAP" to "limits fermentable carbs (for IBS, gut sensitivity)",
            "Carnivore" to "meat-only or meat-dominant",
            "Diabetic / Low Glycemic" to "controls blood sugar via carb choices",
            "Halal" to "foods permitted by Islamic dietary law",
            "Kosher" to "foods prepared in accordance with Jewish law",
            "Nut-Free" to "excludes all nuts (for allergies)",
            "Soy-Free" to "avoids soy products",
            "Alcohol-Free / Sober" to "avoids alcohol entirely",
            "Eco-conscious / Sustainable Eating" to "chooses foods based on environmental impact",
            "Calorie-Restricted" to "limits daily calorie intake",
            "High-Protein" to "focuses on protein-rich foods",
            "Macro-Based / IIFYM (If It Fits Your Macros)" to "tracks protein, carbs, fats",
            "Fruitarian" to "consumes mostly or only raw fruit"
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val firestore = Firebase.firestore
                val lifestylesCollection = firestore.collection("lifestyles")
                
                // Check if lifestyles already exist
                val existingLifestyles = lifestylesCollection.get().await()
                
                if (existingLifestyles.isEmpty) {
                    logi("Initializing lifestyles collection", "ListeryApplication")
                    
                    // Add all lifestyles to Firestore
                    lifestyles.forEach { (name, description) ->
                        lifestylesCollection.document(name.lowercase().replace(" ", "-").replace("/", "-").replace("(", "").replace(")", ""))
                            .set(mapOf(
                                "name" to name,
                                "description" to description,
                                "createdAt" to com.google.firebase.Timestamp.now()
                            ))
                            .await()
                    }
                    
                    logi("Successfully initialized ${lifestyles.size} lifestyles", "ListeryApplication")
                } else {
                    logi("Lifestyles collection already exists with ${existingLifestyles.size()} documents", "ListeryApplication")
                }
            } catch (e: Exception) {
                logi("Error initializing lifestyles: ${e.message}", "ListeryApplication")
            }
        }
    }
}