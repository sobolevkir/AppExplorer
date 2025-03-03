package com.sobolevkir.appexplorer.presentation.screen.installed_apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.appexplorer.domain.model.AppItem
import com.sobolevkir.appexplorer.domain.usecase.GetInstalledAppsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstalledAppsViewModel @Inject constructor(
    private val getInstalledAppsUseCase: GetInstalledAppsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InstalledAppsUiState())
    val uiState: StateFlow<InstalledAppsUiState> = _uiState

    init {
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        viewModelScope.launch {
            val appList = getInstalledAppsUseCase()
            _uiState.update { it.copy(isLoading = false, appList = appList) }
        }
    }

    fun onSearchQueryChanged(query: String) {
        if(query.isEmpty()) {
            _uiState.update {
                it.copy(searchQuery = query, filteredAppList = null)
            }
        }
        _uiState.update {
            it.copy(searchQuery = query, filteredAppList = filterAppList(query))
        }
    }

    private fun filterAppList(query: String): List<AppItem> {
        return _uiState.value.appList.filter { it.appName.contains(query, ignoreCase = true) }
    }

}
