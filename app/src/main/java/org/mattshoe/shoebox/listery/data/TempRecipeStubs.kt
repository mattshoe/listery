//package org.mattshoe.shoebox.listery.data
//
//import org.mattshoe.shoebox.listery.model.Ingredient
//import org.mattshoe.shoebox.listery.model.Recipe
//import org.mattshoe.shoebox.listery.model.RecipeStep
//import kotlin.time.Duration.Companion.hours
//import kotlin.time.Duration.Companion.minutes
//
//val TEMPORARY_RECIPES_STUBS = listOf(
//    Recipe(
//        name = "Avocado toast and eggs",
//        starred = false,
//        url = null,
//        calories = 275,
//        ingredients = listOf(
//            Ingredient(name = "avocado", qty = 1f, unit = "whole"),
//            Ingredient(name = "bread", qty = 2f, unit = "slices"),
//            Ingredient(name = "eggs", qty = 2f, unit = "whole"),
//            Ingredient(name = "salt", qty = 0.5f, unit = "tsp"),
//            Ingredient(name = "pepper", qty = 0.25f, unit = "tsp"),
//            Ingredient(name = "red pepper flakes", qty = 0.125f, unit = "tsp"),
//            Ingredient(name = "lemon juice", qty = 1f, unit = "tbsp"),
//            Ingredient(name = "olive oil", qty = 1f, unit = "tbsp"),
//            Ingredient(name = "garlic powder", qty = 0.25f, unit = "tsp"),
//            Ingredient(name = "everything bagel seasoning", qty = 1f, unit = "tbsp")
//        ),
//        prepTime = 20.minutes,
//        notes = "I have some notes on this and here they are: ooga booga",
//        steps = listOf(
//            RecipeStep(instructions = "First, dance around the kitchen for good luck"),
//            RecipeStep(instructions = "Find the biggest avocado in the store and give it a pep talk"),
//            RecipeStep(instructions = "Toast the bread until it's as crispy as your ex's personality"),
//            RecipeStep(instructions = "Mash the avocado like it owes you money"),
//            RecipeStep(instructions = "Cook the eggs until they're as perfect as your life isn't"),
//            RecipeStep(instructions = "Assemble everything while pretending you're on a cooking show")
//        )
//    ),
//    Recipe(
//        name = "Poop on a stick",
//        starred = false,
//        url = "https://www.poopstick.com",
//        calories = 275,
//        ingredients = listOf(
//            Ingredient(name = "sturdy stick", qty = 1f, unit = "whole"),
//            Ingredient(name = "forest moss", qty = 2f, unit = "handfuls"),
//            Ingredient(name = "tree bark", qty = 1f, unit = "piece"),
//            Ingredient(name = "pine needles", qty = 3f, unit = "sprigs"),
//            Ingredient(name = "mud", qty = 1f, unit = "cup"),
//            Ingredient(name = "leaves", qty = 5f, unit = "pieces"),
//            Ingredient(name = "acorns", qty = 2f, unit = "whole"),
//            Ingredient(name = "twigs", qty = 4f, unit = "pieces")
//        ),
//        prepTime = 45.minutes,
//        notes = "DO NOT TRY THIS AT HOME. Or do, I'm not your mom. But seriously, this is just a joke recipe. Unless...? No, definitely just a joke. Unless...?",
//        steps = listOf(
//            RecipeStep(instructions = "Find the sturdiest stick in the forest"),
//            RecipeStep(instructions = "Practice your stick-waving technique"),
//            RecipeStep(instructions = "Channel your inner caveman"),
//            RecipeStep(instructions = "Make sure no one is watching"),
//            RecipeStep(instructions = "Execute the perfect stick maneuver")
//        )
//    ),
//    Recipe(
//        name = "Kraft mac n cheese",
//        starred = true,
//        url = null,
//        calories = 400,
//        ingredients = listOf(
//            Ingredient(name = "macaroni noodles", qty = 1f, unit = "box"),
//            Ingredient(name = "cheese powder", qty = 1f, unit = "packet"),
//            Ingredient(name = "milk", qty = 0.25f, unit = "cup"),
//            Ingredient(name = "butter", qty = 4f, unit = "tbsp"),
//            Ingredient(name = "salt", qty = 0.5f, unit = "tsp"),
//            Ingredient(name = "black pepper", qty = 0.25f, unit = "tsp"),
//            Ingredient(name = "garlic powder", qty = 0.125f, unit = "tsp"),
//            Ingredient(name = "onion powder", qty = 0.125f, unit = "tsp"),
//            Ingredient(name = "paprika", qty = 0.125f, unit = "tsp"),
//            Ingredient(name = "hot sauce", qty = 1f, unit = "tsp"),
//            Ingredient(name = "breadcrumbs", qty = 0.25f, unit = "cup"),
//            Ingredient(name = "parmesan cheese", qty = 2f, unit = "tbsp")
//        ),
//        prepTime = 20.minutes,
//        notes = """The ultimate comfort food. I remember making this in college at 3am after studying for finals. The orange powder would get everywhere and stain everything, but it was worth it. One time my roommate walked in while I was eating it and said 'That's the most beautiful thing I've ever seen' and I think he was being serious. The power of Kraft Mac n Cheese, folks. It brings people together. Also, pro tip: add a splash of hot sauce and some garlic powder. It'll change your life. Or at least your 3am snack game.""",
//        steps = listOf(
//            RecipeStep(instructions = "Boil water like you're making a potion"),
//            RecipeStep(instructions = "Add noodles and stir counterclockwise exactly 7 times"),
//            RecipeStep(instructions = "Drain water while doing your best pasta-drainer impression"),
//            RecipeStep(instructions = "Add the cheese packet and stir until your arm falls off"),
//            RecipeStep(instructions = "Let it sit for exactly 2 minutes while you contemplate life")
//        )
//    ),
//    Recipe(
//        name = "Cauliflower Pizza",
//        starred = true,
//        url = "https://www.google.com",
//        calories = 700,
//        ingredients = listOf(
//            Ingredient(name = "cauliflower", qty = 1f, unit = "head"),
//            Ingredient(name = "mozzarella cheese", qty = 2f, unit = "cups"),
//            Ingredient(name = "parmesan cheese", qty = 0.5f, unit = "cup"),
//            Ingredient(name = "eggs", qty = 2f, unit = "whole"),
//            Ingredient(name = "garlic powder", qty = 1f, unit = "tsp"),
//            Ingredient(name = "oregano", qty = 1f, unit = "tsp"),
//            Ingredient(name = "basil", qty = 1f, unit = "tsp"),
//            Ingredient(name = "salt", qty = 1f, unit = "tsp"),
//            Ingredient(name = "pepper", qty = 0.5f, unit = "tsp"),
//            Ingredient(name = "pizza sauce", qty = 0.5f, unit = "cup"),
//            Ingredient(name = "olive oil", qty = 2f, unit = "tbsp"),
//            Ingredient(name = "almond flour", qty = 0.25f, unit = "cup"),
//            Ingredient(name = "red pepper flakes", qty = 0.5f, unit = "tsp"),
//            Ingredient(name = "tomatoes", qty = 2f, unit = "whole"),
//            Ingredient(name = "fresh basil", qty = 0.25f, unit = "cup")
//        ),
//        prepTime = 20.minutes,
//        notes = "Note to self: hot dawg. Also, this is NOT pizza. It's a sad, sad attempt at pizza. But it's better than nothing when you're trying to be 'healthy'. The cauliflower will try to betray you at every step - stay strong. And for the love of all things holy, SQUEEZE OUT ALL THE WATER. I cannot stress this enough. Your future self will thank you.",
//        steps = listOf(
//            RecipeStep(instructions = "Grate cauliflower while pretending you're a professional chef"),
//            RecipeStep(instructions = "Squeeze out moisture like you're wringing out your life's regrets"),
//            RecipeStep(instructions = "Mix ingredients until they form a perfect circle of lies"),
//            RecipeStep(instructions = "Bake until it looks nothing like real pizza"),
//            RecipeStep(instructions = "Top with cheese and pretend it's just as good as the real thing"),
//            RecipeStep(instructions = "Serve with a side of denial")
//        )
//    ),
//    Recipe(
//        name = "Papa John's",
//        starred = false,
//        url = "https://www.yourmom.com",
//        calories = 1250,
//        ingredients = listOf(
//            Ingredient(name = "phone", qty = 1f, unit = "whole"),
//            Ingredient(name = "credit card", qty = 1f, unit = "whole"),
//            Ingredient(name = "patience", qty = 20f, unit = "minutes"),
//            Ingredient(name = "hunger", qty = 1f, unit = "whole"),
//            Ingredient(name = "cravings", qty = 1f, unit = "whole"),
//            Ingredient(name = "tip money", qty = 5f, unit = "dollars"),
//            Ingredient(name = "garlic sauce", qty = 2f, unit = "cups"),
//            Ingredient(name = "pepperoncini", qty = 2f, unit = "whole"),
//            Ingredient(name = "parmesan cheese", qty = 1f, unit = "packet"),
//            Ingredient(name = "red pepper flakes", qty = 1f, unit = "packet"),
//            Ingredient(name = "pizza box", qty = 1f, unit = "whole"),
//            Ingredient(name = "napkins", qty = 10f, unit = "pieces")
//        ),
//        prepTime = 1.hours,
//        notes = """mmmmm my fave. Also, here's a story: One time I ordered Papa John's and the delivery guy showed up with a flat tire. He had to walk the last two blocks. I felt so bad I gave him a huge tip and now we're best friends. Just kidding, I never saw him again. But the pizza was still hot! Magic.
//
//Also, pro tip: always order the garlic sauce. Always. Even if you're not getting breadsticks. Just... trust me on this one. Your breath will smell like garlic for days but your soul will be happy.""",
//        steps = listOf(
//            RecipeStep(instructions = "Open your phone and find the Papa John's app"),
//            RecipeStep(instructions = "Scroll through the menu while drooling"),
//            RecipeStep(instructions = "Add extra cheese because you're worth it"),
//            RecipeStep(instructions = "Enter your credit card details with confidence"),
//            RecipeStep(instructions = "Wait by the door like an excited puppy"),
//            RecipeStep(instructions = "Tip the delivery person extra for dealing with your enthusiasm")
//        )
//    ),
//    Recipe(
//        name = "Butter Chicken",
//        starred = false,
//        url = "https://www.butterchicken.com",
//        calories = 650,
//        ingredients = listOf(
//            Ingredient(name = "chicken thighs", qty = 2f, unit = "lbs"),
//            Ingredient(name = "yogurt", qty = 1f, unit = "cup"),
//            Ingredient(name = "garam masala", qty = 2f, unit = "tbsp"),
//            Ingredient(name = "turmeric", qty = 1f, unit = "tsp"),
//            Ingredient(name = "cumin", qty = 1f, unit = "tsp"),
//            Ingredient(name = "coriander", qty = 1f, unit = "tsp"),
//            Ingredient(name = "ginger", qty = 2f, unit = "tbsp"),
//            Ingredient(name = "garlic", qty = 4f, unit = "cloves"),
//            Ingredient(name = "tomato sauce", qty = 2f, unit = "cups"),
//            Ingredient(name = "butter", qty = 0.5f, unit = "cup"),
//            Ingredient(name = "heavy cream", qty = 1f, unit = "cup"),
//            Ingredient(name = "onion", qty = 1f, unit = "large"),
//            Ingredient(name = "cilantro", qty = 0.5f, unit = "cup"),
//            Ingredient(name = "rice", qty = 2f, unit = "cups"),
//            Ingredient(name = "naan bread", qty = 4f, unit = "pieces"),
//            Ingredient(name = "cardamom", qty = 4f, unit = "pods"),
//            Ingredient(name = "cinnamon", qty = 1f, unit = "stick"),
//            Ingredient(name = "chili powder", qty = 1f, unit = "tsp"),
//            Ingredient(name = "salt", qty = 1f, unit = "tsp"),
//            Ingredient(name = "black pepper", qty = 0.5f, unit = "tsp")
//        ),
//        prepTime = 1.hours + 10.minutes,
//        notes = """This recipe is the result of my 3-year journey to recreate the butter chicken from that one Indian restaurant I went to that one time. I've made it 47 times. I'm getting closer, but I'm still not there. The restaurant won't give me their recipe (rude) so I've had to figure it out through trial and error. Mostly error.
//
//The key is to marinate the chicken for at least 4 hours, but overnight is better. Also, don't skimp on the butter. The name is literally 'Butter Chicken' - this is not the time to be counting calories. If you're worried about calories, maybe don't make butter chicken?
//
//Fun fact: I once made this for a date and they proposed. I said no, but kept the recipe. Priorities, people.""",
//        steps = listOf(
//            RecipeStep(instructions = "Marinate chicken in yogurt and spices while doing yoga"),
//            RecipeStep(instructions = "Cook onions until they're as caramelized as your personality"),
//            RecipeStep(instructions = "Add tomatoes and pretend you're in India"),
//            RecipeStep(instructions = "Simmer sauce until it's as smooth as your pickup lines"),
//            RecipeStep(instructions = "Add butter and cream because calories don't count on weekends"),
//            RecipeStep(instructions = "Garnish with cilantro and your hopes and dreams")
//        )
//    ),
//    Recipe(
//        name = "Sunday WingDay",
//        starred = true,
//        url = "https://www.sundayfunday.com",
//        calories = 400,
//        ingredients = listOf(
//            Ingredient(name = "chicken wings", qty = 2f, unit = "lbs"),
//            Ingredient(name = "hot sauce", qty = 0.5f, unit = "cup"),
//            Ingredient(name = "butter", qty = 0.25f, unit = "cup"),
//            Ingredient(name = "garlic powder", qty = 1f, unit = "tsp"),
//            Ingredient(name = "onion powder", qty = 1f, unit = "tsp"),
//            Ingredient(name = "paprika", qty = 1f, unit = "tsp"),
//            Ingredient(name = "cayenne pepper", qty = 0.5f, unit = "tsp"),
//            Ingredient(name = "salt", qty = 1f, unit = "tsp"),
//            Ingredient(name = "black pepper", qty = 0.5f, unit = "tsp"),
//            Ingredient(name = "blue cheese", qty = 0.5f, unit = "cup"),
//            Ingredient(name = "celery", qty = 4f, unit = "stalks"),
//            Ingredient(name = "carrots", qty = 4f, unit = "whole"),
//            Ingredient(name = "ranch dressing", qty = 0.5f, unit = "cup"),
//            Ingredient(name = "beer", qty = 6f, unit = "cans"),
//            Ingredient(name = "football game", qty = 1f, unit = "whole")
//        ),
//        prepTime = 35.minutes,
//        notes = "Kayla doesn't eat meat anymore :( But that's okay, more wings for me! This recipe is perfect for game day, or any day that ends in 'y'. The secret is in the sauce - I use a combination of three different hot sauces because I'm extra like that. Also, make sure to have plenty of napkins on hand. And maybe a fire extinguisher. Just in case.",
//        steps = listOf(
//            RecipeStep(instructions = "Defrost wings while watching football highlights"),
//            RecipeStep(instructions = "Season wings with enough spice to make your ancestors proud"),
//            RecipeStep(instructions = "Bake until crispy like your sense of humor"),
//            RecipeStep(instructions = "Make blue cheese dressing from scratch (just kidding, use store-bought)"),
//            RecipeStep(instructions = "Serve with celery sticks to feel healthy"),
//            RecipeStep(instructions = "Eat while watching the game and ignoring your diet")
//        )
//    ),
//    Recipe(
//        name = "Spaghetti O's",
//        starred = false,
//        url = "https://www.google.com",
//        calories = 200,
//        ingredients = listOf(
//            Ingredient(name = "Spaghetti O's can", qty = 1f, unit = "whole"),
//            Ingredient(name = "can opener", qty = 1f, unit = "whole"),
//            Ingredient(name = "bowl", qty = 1f, unit = "whole"),
//            Ingredient(name = "microwave", qty = 1f, unit = "whole"),
//            Ingredient(name = "spoon", qty = 1f, unit = "whole"),
//            Ingredient(name = "nostalgia", qty = 1f, unit = "whole"),
//            Ingredient(name = "childhood memories", qty = 1f, unit = "whole"),
//            Ingredient(name = "regret", qty = 1f, unit = "whole"),
//            Ingredient(name = "shame", qty = 1f, unit = "whole"),
//            Ingredient(name = "pride", qty = 0f, unit = "whole")
//        ),
//        prepTime = 5.minutes,
//        notes = """I'm not proud of this recipe, but I'm not ashamed either. Sometimes you just need a can of Spaghetti O's in your life. It's like a warm hug from your childhood, if your childhood was made of processed food and questionable nutritional value.
//
//I keep a can in my pantry at all times. It's my emergency food. My comfort food. My 'I'm too lazy to cook' food. My 'I'm too sad to cook' food. My 'I'm too happy to cook' food. Basically, it's my everything food.
//
//The best way to eat it is straight from the can, standing in your kitchen at 2am, questioning your life choices. But that's just me.""",
//        steps = listOf(
//            RecipeStep(instructions = "Open can with the can opener you can never find"),
//            RecipeStep(instructions = "Pour into bowl while pretending you're a gourmet chef"),
//            RecipeStep(instructions = "Microwave until it's as hot as your coffee that's now cold"),
//            RecipeStep(instructions = "Stir and burn your mouth because you're too impatient"),
//            RecipeStep(instructions = "Enjoy your childhood favorite while questioning your life choices")
//        )
//    ),
//    Recipe(
//        name = "Fish and Chips",
//        starred = true,
//        url = "https://www.reducetarian.com",
//        calories = 525,
//        ingredients = listOf(
//            Ingredient(name = "cod fillets", qty = 4f, unit = "pieces"),
//            Ingredient(name = "potatoes", qty = 4f, unit = "large"),
//            Ingredient(name = "flour", qty = 2f, unit = "cups"),
//            Ingredient(name = "beer", qty = 1f, unit = "cup"),
//            Ingredient(name = "baking powder", qty = 1f, unit = "tsp"),
//            Ingredient(name = "salt", qty = 2f, unit = "tsp"),
//            Ingredient(name = "pepper", qty = 1f, unit = "tsp"),
//            Ingredient(name = "vegetable oil", qty = 4f, unit = "cups"),
//            Ingredient(name = "malt vinegar", qty = 0.25f, unit = "cup"),
//            Ingredient(name = "tartar sauce", qty = 0.5f, unit = "cup"),
//            Ingredient(name = "lemon", qty = 1f, unit = "whole"),
//            Ingredient(name = "parsley", qty = 0.25f, unit = "cup"),
//            Ingredient(name = "paprika", qty = 0.5f, unit = "tsp"),
//            Ingredient(name = "garlic powder", qty = 0.5f, unit = "tsp"),
//            Ingredient(name = "onion powder", qty = 0.5f, unit = "tsp"),
//            Ingredient(name = "British accent", qty = 1f, unit = "whole"),
//            Ingredient(name = "rain", qty = 1f, unit = "whole"),
//            Ingredient(name = "newspaper", qty = 1f, unit = "sheet")
//        ),
//        prepTime = 45.minutes,
//        notes = "The key to authentic fish and chips is to eat it while it's raining. I don't make the rules, I just follow them. Also, make sure to wrap it in newspaper for that authentic British experience. If you don't have a newspaper, just print out some random Wikipedia articles. The fish won't know the difference.",
//        steps = listOf(
//            RecipeStep(instructions = "Batter fish while pretending you're in a British pub"),
//            RecipeStep(instructions = "Cut potatoes into chips while practicing your British accent"),
//            RecipeStep(instructions = "Fry everything until it's as golden as your tan from 2010"),
//            RecipeStep(instructions = "Drain on paper towels like a proper chef"),
//            RecipeStep(instructions = "Serve with malt vinegar and a side of regret"),
//            RecipeStep(instructions = "Enjoy while watching the rain outside your window")
//        )
//    ),
//    Recipe(
//        name = "Empty Recipe",
//        starred = false,
//        url = null,
//        calories = 0,
//        ingredients = emptyList(),
//        prepTime = null,
//        notes = null,
//        steps = emptyList()
//    ),
//    Recipe(
//        name = "URL Only Recipe",
//        starred = false,
//        url = "https://www.example.com/recipe",
//        calories = 0,
//        ingredients = emptyList(),
//        prepTime = null,
//        notes = null,
//        steps = emptyList()
//    ),
//    Recipe(
//        name = "Notes Only Recipe",
//        starred = false,
//        url = null,
//        calories = 0,
//        ingredients = emptyList(),
//        prepTime = null,
//        notes = "This recipe exists only to test notes",
//        steps = emptyList()
//    ),
//    Recipe(
//        name = "Breakfast of Champions",
//        starred = true,
//        url = "https://www.breakfast.com/champions",
//        calories = 1200,
//        ingredients = listOf(
//            Ingredient(name = "coffee", qty = 4f, unit = "cups"),
//            Ingredient(name = "regret", qty = 1f, unit = "whole"),
//            Ingredient(name = "determination", qty = 1f, unit = "whole")
//        ),
//        prepTime = 5.minutes,
//        notes = "The only breakfast that matters",
//        steps = listOf(
//            RecipeStep(instructions = "Brew coffee"),
//            RecipeStep(instructions = "Question life choices"),
//            RecipeStep(instructions = "Drink coffee")
//        )
//    ),
//    Recipe(
//        name = "Microwave Burrito",
//        starred = false,
//        url = null,
//        calories = 450,
//        ingredients = listOf(
//            Ingredient(name = "frozen burrito", qty = 1f, unit = "whole"),
//            Ingredient(name = "hope", qty = 1f, unit = "whole")
//        ),
//        prepTime = 3.minutes,
//        notes = "Sometimes you just need a burrito",
//        steps = listOf(
//            RecipeStep(instructions = "Remove plastic"),
//            RecipeStep(instructions = "Microwave"),
//            RecipeStep(instructions = "Burn mouth")
//        )
//    ),
//    Recipe(
//        name = "Fancy Water",
//        starred = true,
//        url = "https://www.fancywater.com",
//        calories = 0,
//        ingredients = listOf(
//            Ingredient(name = "water", qty = 1f, unit = "glass"),
//            Ingredient(name = "cucumber", qty = 2f, unit = "slices"),
//            Ingredient(name = "mint", qty = 3f, unit = "leaves"),
//            Ingredient(name = "pretentiousness", qty = 1f, unit = "whole")
//        ),
//        prepTime = 2.minutes,
//        notes = "It's just water, but make it fancy",
//        steps = listOf(
//            RecipeStep(instructions = "Fill glass with water"),
//            RecipeStep(instructions = "Add cucumber and mint"),
//            RecipeStep(instructions = "Take Instagram photo"),
//            RecipeStep(instructions = "Drink water")
//        )
//    ),
//    Recipe(
//        name = "Depression Meal",
//        starred = false,
//        url = null,
//        calories = 150,
//        ingredients = listOf(
//            Ingredient(name = "crackers", qty = 1f, unit = "sleeve"),
//            Ingredient(name = "tears", qty = 1f, unit = "cup")
//        ),
//        prepTime = 1.minutes,
//        notes = "Sometimes you just need to cry into your crackers",
//        steps = listOf(
//            RecipeStep(instructions = "Open crackers"),
//            RecipeStep(instructions = "Cry"),
//            RecipeStep(instructions = "Eat crackers")
//        )
//    ),
//    Recipe(
//        name = "Midnight Snack",
//        starred = true,
//        url = null,
//        calories = 800,
//        ingredients = listOf(
//            Ingredient(name = "leftovers", qty = 1f, unit = "container"),
//            Ingredient(name = "shame", qty = 1f, unit = "whole"),
//            Ingredient(name = "darkness", qty = 1f, unit = "whole")
//        ),
//        prepTime = 2.minutes,
//        notes = "The best meals are eaten in the dark",
//        steps = listOf(
//            RecipeStep(instructions = "Open fridge"),
//            RecipeStep(instructions = "Find leftovers"),
//            RecipeStep(instructions = "Eat in darkness"),
//            RecipeStep(instructions = "Question life choices")
//        )
//    ),
//    Recipe(
//        name = "Bachelor Chow",
//        starred = false,
//        url = "https://www.futurama.com/recipes",
//        calories = 2000,
//        ingredients = listOf(
//            Ingredient(name = "ramen", qty = 4f, unit = "packages"),
//            Ingredient(name = "hot sauce", qty = 1f, unit = "bottle"),
//            Ingredient(name = "desperation", qty = 1f, unit = "whole")
//        ),
//        prepTime = 10.minutes,
//        notes = "Now with flavor!",
//        steps = listOf(
//            RecipeStep(instructions = "Boil water"),
//            RecipeStep(instructions = "Add ramen"),
//            RecipeStep(instructions = "Add hot sauce"),
//            RecipeStep(instructions = "Eat directly from pot")
//        )
//    ),
//    Recipe(
//        name = "Emergency Chocolate",
//        starred = true,
//        url = null,
//        calories = 500,
//        ingredients = listOf(
//            Ingredient(name = "chocolate", qty = 1f, unit = "bar"),
//            Ingredient(name = "emergency", qty = 1f, unit = "whole")
//        ),
//        prepTime = 0.minutes,
//        notes = "For emergency use only",
//        steps = listOf(
//            RecipeStep(instructions = "Open chocolate"),
//            RecipeStep(instructions = "Eat chocolate"),
//            RecipeStep(instructions = "Feel better")
//        )
//    ),
//    Recipe(
//        name = "Air",
//        starred = false,
//        url = null,
//        calories = 0,
//        ingredients = emptyList(),
//        prepTime = null,
//        notes = "The most important ingredient in any recipe",
//        steps = emptyList()
//    ),
//    Recipe(
//        name = "Recipe with No Name",
//        starred = false,
//        url = null,
//        calories = 0,
//        ingredients = emptyList(),
//        prepTime = null,
//        notes = "This recipe has no name",
//        steps = emptyList()
//    ),
//    Recipe(
//        name = "Recipe with No Steps",
//        starred = false,
//        url = null,
//        calories = 0,
//        ingredients = listOf(
//            Ingredient(name = "ingredient", qty = 1f, unit = "whole")
//        ),
//        prepTime = null,
//        notes = "This recipe has no steps",
//        steps = emptyList()
//    ),
//    Recipe(
//        name = "Recipe with No Ingredients",
//        starred = false,
//        url = null,
//        calories = 0,
//        ingredients = emptyList(),
//        prepTime = null,
//        notes = "This recipe has no ingredients",
//        steps = listOf(
//            RecipeStep(instructions = "Step 1"),
//            RecipeStep(instructions = "Step 2")
//        )
//    ),
//    Recipe(
//        name = "Recipe with Everything",
//        starred = true,
//        url = "https://www.example.com/recipe",
//        calories = 1000,
//        ingredients = listOf(
//            Ingredient(name = "ingredient", qty = 1f, unit = "whole")
//        ),
//        prepTime = 1.hours,
//        notes = "This recipe has everything",
//        steps = listOf(
//            RecipeStep(instructions = "Step 1"),
//            RecipeStep(instructions = "Step 2")
//        )
//    )
//)