package com.sobolevkir.appexplorer.presentation.screen.installed_apps

import com.sobolevkir.appexplorer.domain.model.AppItem

data class InstalledAppsUiState(

    val isLoading: Boolean = true,
    val appList: List<AppItem> = emptyList(),
    val searchQuery: String = "",
    val filteredAppList: List<AppItem>? = null

)