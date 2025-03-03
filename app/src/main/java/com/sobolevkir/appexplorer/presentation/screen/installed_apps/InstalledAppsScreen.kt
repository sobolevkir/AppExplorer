package com.sobolevkir.appexplorer.presentation.screen.installed_apps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sobolevkir.appexplorer.R
import com.sobolevkir.appexplorer.presentation.navigation.Route
import com.sobolevkir.appexplorer.presentation.screen.installed_apps.component.AppItemRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstalledAppsScreen(
    onNavigateTo: (Route) -> Unit = {}
) {
    val viewModel: InstalledAppsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(R.string.title_installed_apps))
            })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), contentAlignment = Alignment.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(uiState.appList) { appItem ->
                        AppItemRow(appItem) {
                            onNavigateTo(Route.AppDetailsRoute(packageName = appItem.packageName))
                        }
                    }
                }
            }
        }
    }
}
