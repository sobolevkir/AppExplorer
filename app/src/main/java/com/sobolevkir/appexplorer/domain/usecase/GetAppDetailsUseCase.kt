package com.sobolevkir.appexplorer.domain.usecase

import com.sobolevkir.appexplorer.domain.api.AppsRepository
import com.sobolevkir.appexplorer.domain.model.AppDetails
import javax.inject.Inject

class GetAppDetailsUseCase @Inject constructor(
    private val repository: AppsRepository
) {

    suspend operator fun invoke(packageName: String): AppDetails? {
        return repository.getAppDetails(packageName)
    }

}