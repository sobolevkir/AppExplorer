package com.sobolevkir.appexplorer.data

import android.content.Context
import com.sobolevkir.appexplorer.domain.api.ExternalNavigator
import javax.inject.Inject

class ExternalNavigatorImpl @Inject constructor(
    private val context: Context
) : ExternalNavigator {
    override fun openApp(packageName: String) {

        val intent = context.packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            context.startActivity(intent)
        }
    }
}