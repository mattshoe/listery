package org.mattshoe.shoebox.listery.ai.common

private const val schemaKey = "\$schema"

val RecipeJsonSchema = """
    {
      "$schemaKey": "http://json-schema.org/draft-07/schema#",
      "title": "Recipe",
      "description": "A recipe with ingredients, steps, and metadata",
      "type": "object",
      "properties": {
        "id": {
          "type": "string",
          "description": "Unique identifier for the recipe",
          "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}${'$'}",
          "default": "auto-generated UUID"
        },
        "name": {
          "type": "string",
          "description": "Name of the recipe",
          "minLength": 1
        },
        "starred": {
          "type": "boolean",
          "description": "Whether the recipe is starred/favorited",
          "default": false
        },
        "url": {
          "type": "string",
          "description": "URL to the original recipe source",
          "format": "uri",
          "default": ""
        },
        "ingredients": {
          "type": "array",
          "description": "List of ingredients required for the recipe",
          "default": [],
          "items": {
            "type": "object",
            "properties": {
              "id": {
                "type": "string",
                "description": "Unique identifier for the ingredient",
                "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}${'$'}",
                "default": "auto-generated UUID"
              },
              "name": {
                "type": "string",
                "description": "Name of the ingredient",
                "minLength": 1
              },
              "qty": {
                "type": "number",
                "description": "Quantity of the ingredient",
                "minimum": 0,
                "default": 0
              },
              "unit": {
                "type": "string",
                "description": "Unit of measurement for the ingredient",
                "minLength": 1
              },
              "calories": {
                "type": "integer",
                "description": "Calories per unit of this ingredient",
                "minimum": 0,
                "default": 0
              }
            },
            "required": ["name", "qty", "unit", "calories"],
            "additionalProperties": false
          }
        },
        "prepTime": {
          "type": "number",
          "description": "Preparation time in seconds (Kotlin Duration converted to seconds)",
          "minimum": 0,
          "default": 0
        },
        "servings": {
          "type": "number",
          "description": "The number of servings contained in the entire recipe.",
          "minimum": 1,
          "default": 1
        },
        "notes": {
          "type": "string",
          "description": "Notes about the recipe",
          "default": ""
        },
        "steps": {
          "type": "array",
          "description": "List of cooking steps/instructions",
          "default": [],
          "items": {
            "type": "object",
            "properties": {
              "instructions": {
                "type": "string",
                "description": "The cooking instruction text",
                "minLength": 1
              },
              "id": {
                "type": "string",
                "description": "Unique identifier for the step",
                "pattern": "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}${'$'}",
                "default": "auto-generated UUID"
              }
            },
            "required": ["instructions"],
            "additionalProperties": false
          }
        },
        "calories": {
          "type": "integer",
          "description": "Total calories calculated from all ingredients",
          "minimum": 0,
          "default": 0,
          "readOnly": true
        }
      },
      "required": ["name", "starred", "ingredients", "steps"],
      "additionalProperties": false
    }
""".trimIndent()