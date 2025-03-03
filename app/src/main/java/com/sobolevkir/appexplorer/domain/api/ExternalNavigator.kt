package com.sobolevkir.appexplorer.domain.api

interface ExternalNavigator {

    fun openApp(packageName: String)

}