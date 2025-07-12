package org.mattshoe.shoebox.listery.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Serializable
data class Recipe(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val starred: Boolean,
    val url: String?,
    val ingredients: List<Ingredient>,
    @Serializable(with = PrepTimeSerializer::class)
    val prepTime: Duration?,
    val servings: Int,
    val notes: String?,
    val steps: List<RecipeStep>
) {
    val calories = ingredients.sumOf { it.calories } / servings
}

@Serializable
data class Ingredient(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val qty: Float,
    val unit: String,
    val calories: Int
)

@Serializable
data class RecipeStep(
    val instructions: String,
    val id: String = UUID.randomUUID().toString()
)

object PrepTimeSerializer : KSerializer<Duration> {

    override val descriptor: SerialDescriptor = kotlinx.serialization.descriptors.PrimitiveSerialDescriptor("Duration", kotlinx.serialization.descriptors.PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder): Duration =
        decoder.decodeLong().seconds

    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeLong(value.inWholeSeconds)
    }
}