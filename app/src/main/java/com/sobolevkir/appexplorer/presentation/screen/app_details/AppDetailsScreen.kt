package com.sobolevkir.appexplorer.presentation.screen.app_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sobolevkir.appexplorer.R
import com.sobolevkir.appexplorer.presentation.navigation.Route
import com.sobolevkir.appexplorer.presentation.screen.app_details.component.AppDetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailsScreen(
    onNavigateTo: (Route) -> Unit = {},
) {

    val viewModel: AppDetailsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_app_details)) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateTo(Route.BackRoute) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                }
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
                uiState.appDetails == null -> Text(
                    stringResource(R.string.message_error),
                    color = MaterialTheme.colorScheme.error
                )

                else -> uiState.appDetails?.let {
                    AppDetailsContent(it, onOpenAppButtonClick = { viewModel.openApp() })
                }
            }
        }
    }
}

