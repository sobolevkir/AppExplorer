package com.sobolevkir.appexplorer.presentation.screen.app_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.appexplorer.domain.usecase.GetAppDetailsUseCase
import com.sobolevkir.appexplorer.domain.usecase.OpenAppUseCase
import com.sobolevkir.appexplorer.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAppDetailsUseCase: GetAppDetailsUseCase,
    private val openAppUseCase: OpenAppUseCase
) : ViewModel() {

    private val packageName = Route.AppDetailsRoute.from(savedStateHandle).packageName

    private val _uiState = MutableStateFlow(AppDetailsUiState())
    val uiState: StateFlow<AppDetailsUiState> = _uiState

    init {
        loadAppDetails(packageName)
    }

    fun openApp() {
        openAppUseCase(packageName)
    }

    private fun loadAppDetails(packageName: String) {
        viewModelScope.launch {
            val appDetails = getAppDetailsUseCase(packageName)
            _uiState.update { it.copy(isLoading = false, appDetails = appDetails) }
        }
    }
}
