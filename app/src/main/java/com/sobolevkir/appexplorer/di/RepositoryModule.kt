package com.sobolevkir.appexplorer.di

import com.sobolevkir.appexplorer.data.AppsRepositoryImpl
import com.sobolevkir.appexplorer.domain.AppsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindAppsRepository(appsRepositoryImpl: AppsRepositoryImpl): AppsRepository

}