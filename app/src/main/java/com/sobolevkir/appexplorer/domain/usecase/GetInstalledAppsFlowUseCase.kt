package com.sobolevkir.appexplorer.domain.usecase

import com.sobolevkir.appexplorer.domain.api.AppsRepository
import com.sobolevkir.appexplorer.domain.model.AppItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetInstalledAppsFlowUseCase @Inject constructor(private val repository: AppsRepository) {

    operator fun invoke(): Flow<List<AppItem>> {
        return repository.getInstalledAppsFlow()
    }

}