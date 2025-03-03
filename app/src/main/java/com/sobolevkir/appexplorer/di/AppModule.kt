package com.sobolevkir.appexplorer.di

import android.content.Context
import com.sobolevkir.appexplorer.data.ExternalNavigatorImpl
import com.sobolevkir.appexplorer.domain.api.ExternalNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideExternalNavigator(context: Context): ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

}