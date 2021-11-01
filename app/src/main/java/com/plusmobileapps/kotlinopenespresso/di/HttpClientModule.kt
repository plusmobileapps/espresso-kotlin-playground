package com.plusmobileapps.kotlinopenespresso.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.android.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HttpEngineModule {
    @Provides
    @Singleton
    fun providesHttpEngine(): HttpClientEngine = Android.create()
}

@Module
@InstallIn(SingletonComponent::class)
object HttpClientModule {

    @Provides
    @Singleton
    fun providesHttpClient(httpClientEngine: HttpClientEngine): HttpClient =
        HttpClient(httpClientEngine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }

}