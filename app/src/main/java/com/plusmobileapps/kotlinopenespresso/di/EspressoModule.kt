package com.plusmobileapps.kotlinopenespresso.di

import com.plusmobileapps.kotlinopenespresso.util.CountingIdlingResource
import com.plusmobileapps.kotlinopenespresso.util.CountingIdlingResourceStub
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class EspressoModule {

    @Singleton
    @Binds
    abstract fun provideCountingIdlingResource(idlingResourceStub: CountingIdlingResourceStub): CountingIdlingResource

}