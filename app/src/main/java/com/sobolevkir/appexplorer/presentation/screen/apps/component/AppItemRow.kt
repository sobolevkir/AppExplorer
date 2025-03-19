package com.sobolevkir.appexplorer.presentation.screen.apps.component

import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sobolevkir.appexplorer.domain.model.AppItem

@Composable
fun AppItemRow(appItem: AppItem, onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon = remember(appItem.packageName) {
            try {
                context.packageManager.getApplicationIcon(appItem.packageName)
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
        }
        icon?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.FillWidth
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(appItem.appName, style = MaterialTheme.typography.bodyLarge)
    }
}