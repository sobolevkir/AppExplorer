package com.sobolevkir.appexplorer.presentation.screen.installed_apps.component

import android.net.Uri
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sobolevkir.appexplorer.domain.model.AppItem

@Composable
fun AppItemRow(appItem: AppItem, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val painter = appItem.appIconUri?.let { rememberAsyncImagePainter(Uri.parse(it)) }
            ?: painterResource(id = android.R.drawable.ic_menu_info_details)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(appItem.appName, style = MaterialTheme.typography.bodyLarge)
    }
}