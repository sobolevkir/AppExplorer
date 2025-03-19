package com.sobolevkir.appexplorer.presentation.screen.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.appexplorer.domain.usecase.GetInstalledAppsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val getInstalledAppsFlowUseCase: GetInstalledAppsFlowUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppsUiState())
    val uiState: StateFlow<AppsUiState> = _uiState

    init {
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        viewModelScope.launch {
            getInstalledAppsFlowUseCase().collect { appList ->
                _uiState.update { it.copy(isLoading = false, appList = appList) }
            }
        }
    }

}
