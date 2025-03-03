package com.sobolevkir.appexplorer.presentation.screen.app_details

import com.sobolevkir.appexplorer.domain.model.AppDetails

data class AppDetailsUiState (
    val isLoading: Boolean = true,
    val appDetails: AppDetails? = null
)