package com.sobolevkir.appexplorer.presentation.screen.apps

import com.sobolevkir.appexplorer.domain.model.AppItem

data class AppsUiState(
    val isLoading: Boolean = true,
    val appList: List<AppItem> = emptyList(),
    )