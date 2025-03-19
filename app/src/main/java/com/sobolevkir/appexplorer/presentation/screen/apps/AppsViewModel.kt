package com.sobolevkir.appexplorer.presentation.screen.apps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.appexplorer.domain.usecase.GetInstalledAppsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val getInstalledAppsFlowUseCase: GetInstalledAppsFlowUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppsUiState())
    val uiState = _uiState.onStart { loadInstalledApps() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppsUiState())

    private fun loadInstalledApps() {
        Log.d("123123123", "sdasdadfsdf")
        viewModelScope.launch {
            getInstalledAppsFlowUseCase().collect { appList ->
                _uiState.update { it.copy(isLoading = false, appList = appList) }
            }
        }
    }

}
