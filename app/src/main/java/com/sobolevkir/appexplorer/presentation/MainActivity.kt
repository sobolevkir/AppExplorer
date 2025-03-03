package com.sobolevkir.appexplorer.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.sobolevkir.appexplorer.presentation.navigation.AppNavGraph
import com.sobolevkir.appexplorer.presentation.theme.AppExplorerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppExplorerTheme {
                AppNavGraph(navController = rememberNavController())
            }
        }
    }
}