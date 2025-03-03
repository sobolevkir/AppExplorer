package com.sobolevkir.appexplorer.domain.usecase

import com.sobolevkir.appexplorer.domain.api.ExternalNavigator
import javax.inject.Inject

class OpenAppUseCase @Inject constructor(private val externalNavigator: ExternalNavigator) {

    operator fun invoke(packageName: String) {
        externalNavigator.openApp(packageName)
    }

}