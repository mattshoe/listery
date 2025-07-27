package org.mattshoe.shoebox.listery.ai.parseweb.usecase

import kotlinx.serialization.Serializable

@Serializable
data class BlacklistedSite(
    val domain: String,
    val reason: String
)