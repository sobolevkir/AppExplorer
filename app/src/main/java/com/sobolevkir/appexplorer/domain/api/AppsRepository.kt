package com.sobolevkir.appexplorer.domain.api

import com.sobolevkir.appexplorer.domain.model.AppDetails
import com.sobolevkir.appexplorer.domain.model.AppItem
import kotlinx.coroutines.flow.Flow

interface AppsRepository {

    fun getInstalledAppsFlow(): Flow<List<AppItem>>
    suspend fun getAppDetails(packageName: String): AppDetails?

}