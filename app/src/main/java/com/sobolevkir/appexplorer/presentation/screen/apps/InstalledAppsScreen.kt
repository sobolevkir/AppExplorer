package com.sobolevkir.appexplorer.presentation.screen.apps

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sobolevkir.appexplorer.R
import com.sobolevkir.appexplorer.presentation.navigation.Route
import com.sobolevkir.appexplorer.presentation.screen.apps.component.AppItemRow
import com.sobolevkir.appexplorer.presentation.screen.apps.component.QueryTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstalledAppsScreen(
    onNavigateTo: (Route) -> Unit = {}
) {
    val viewModel: InstalledAppsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val filteredItems = remember(searchQuery, uiState.appList) {
        uiState.appList.filter { it.appName.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_installed_apps)) }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator()
                uiState.appList.isEmpty() -> Text(
                    stringResource(R.string.message_error),
                    color = MaterialTheme.colorScheme.error
                )

                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        QueryTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it }
                        )
                        if (filteredItems.isEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(R.string.message_empty),
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn {
                                items(filteredItems) { appItem ->
                                    AppItemRow(appItem) {
                                        onNavigateTo(Route.AppDetailsRoute(packageName = appItem.packageName))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
