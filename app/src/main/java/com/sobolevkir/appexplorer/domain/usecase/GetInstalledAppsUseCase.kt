package com.sobolevkir.appexplorer.domain.usecase

import com.sobolevkir.appexplorer.domain.api.AppsRepository
import com.sobolevkir.appexplorer.domain.model.AppItem
import javax.inject.Inject

class GetInstalledAppsUseCase @Inject constructor(private val repository: AppsRepository) {

    suspend operator fun invoke(): List<AppItem> {
        return repository.getInstalledApps()
    }

}