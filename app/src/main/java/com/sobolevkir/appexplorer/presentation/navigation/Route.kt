package com.sobolevkir.appexplorer.presentation.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    object BackRoute : Route

    @Serializable
    object InstalledAppsRoute : Route

    @Serializable
    data class AppDetailsRoute(val packageName: String) : Route {
        companion object {
            fun from(savedStateHandle: SavedStateHandle): AppDetailsRoute {
                return savedStateHandle.toRoute()
            }
        }
    }

}