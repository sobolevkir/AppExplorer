package com.sobolevkir.appexplorer.presentation.screen.app_details.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.sobolevkir.appexplorer.R
import com.sobolevkir.appexplorer.domain.model.AppDetails

@Composable
fun AppDetailsContent(appDetails: AppDetails) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val context = LocalContext.current
        val painter =
            appDetails.iconStringUri?.let { rememberAsyncImagePainter(Uri.parse(it)) }
                ?: painterResource(id = android.R.drawable.ic_menu_info_details)
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = appDetails.appName,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            stringResource(R.string.app_details_package_name, appDetails.packageName),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(R.string.app_details_version, appDetails.version),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            stringResource(R.string.app_details_sha256, appDetails.apkSha256),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                val intent =
                    context.packageManager.getLaunchIntentForPackage(appDetails.packageName)
                if (intent != null) {
                    context.startActivity(intent)
                }
            }) {
            Text(stringResource(R.string.btn_open_app))
        }
    }
}