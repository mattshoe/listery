package org.mattshoe.shoebox.listery.ai.di

import com.aallam.openai.api.http.Timeout
import com.aallam.openai.client.OpenAI
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mattshoe.shoebox.listery.config.RemoteConfigRepository
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
interface AiModule {

    companion object {
        @Provides
        @Reusable
        fun providesOpenAi(remoteConfigRepository: RemoteConfigRepository): OpenAI = OpenAI(
            token = remoteConfigRepository.getString("OPENAI_KEY"),
            timeout = Timeout(socket = 60.seconds),
        )
    }
}