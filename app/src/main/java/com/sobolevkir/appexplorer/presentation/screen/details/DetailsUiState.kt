package com.sobolevkir.appexplorer.presentation.screen.details

import com.sobolevkir.appexplorer.domain.model.AppDetails

data class DetailsUiState(
    val isLoading: Boolean = true,
    val appDetails: AppDetails? = null,
)