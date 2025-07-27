package org.mattshoe.shoebox.listery.ai.parseweb.usecase

import kotlinx.serialization.Serializable
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

@Serializable
data class DomainBlacklist(
    val sites: List<BlacklistedSite>
) {
    fun entry(url: String): BlacklistedSite? {
        return url.toHttpUrlOrNull()?.topPrivateDomain()?.let { domain ->
            sites.firstOrNull {
                it.domain == domain
            }
        }
    }
}