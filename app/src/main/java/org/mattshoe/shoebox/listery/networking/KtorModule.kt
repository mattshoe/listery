package org.mattshoe.shoebox.listery.networking

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import okhttp3.Protocol
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface KtorModule {
    companion object {
        @Provides
        @Singleton
        fun providesHttpClient(): HttpClient {
            return HttpClient(OkHttp) {
                engine {
                    config {
                        protocols(listOf(Protocol.HTTP_1_1))
                    }
                }
                defaultRequest {
                    headers.append(HttpHeaders.UserAgent, System.getProperty("http.agent") ?: "Listery/1.0 (Android; Kotlin; okhttp)")
                }
            }
        }
    }
}