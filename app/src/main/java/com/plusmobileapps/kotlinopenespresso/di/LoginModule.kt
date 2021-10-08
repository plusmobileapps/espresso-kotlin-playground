package com.plusmobileapps.kotlinopenespresso.di

import com.plusmobileapps.kotlinopenespresso.data.LoginDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object LoginModule {

    @Provides
    fun provideLoginDataSource(): LoginDataSource = LoginDataSource()

}