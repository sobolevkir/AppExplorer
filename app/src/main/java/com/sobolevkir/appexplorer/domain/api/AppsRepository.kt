package com.sobolevkir.appexplorer.domain.api

import com.sobolevkir.appexplorer.domain.model.AppDetails
import com.sobolevkir.appexplorer.domain.model.AppItem

interface AppsRepository {

    suspend fun getInstalledApps(): List<AppItem>
    suspend fun getAppDetails(packageName: String): AppDetails?

}