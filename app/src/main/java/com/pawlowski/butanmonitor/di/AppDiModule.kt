package com.pawlowski.butanmonitor.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object AppDiModule {
    @Provides
    fun context(application: Application): Context = application.applicationContext
}
