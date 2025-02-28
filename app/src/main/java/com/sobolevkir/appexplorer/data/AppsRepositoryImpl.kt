package com.sobolevkir.appexplorer.data

import android.content.Context
import android.content.pm.PackageManager
import com.sobolevkir.appexplorer.domain.AppsRepository
import com.sobolevkir.appexplorer.domain.model.AppDetails
import com.sobolevkir.appexplorer.domain.model.AppItem
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.security.MessageDigest
import javax.inject.Inject

class AppsRepositoryImpl @Inject constructor(
    private val context: Context
) : AppsRepository {

    private val packageManager: PackageManager
        get() = context.packageManager

    override suspend fun getInstalledApps(): List<AppItem> = withContext(Dispatchers.IO) {
        val packages = packageManager.getInstalledPackages(0)
        packages.map { packageInfo ->
            AppItem(
                packageName = packageInfo.packageName,
                appName = packageInfo.applicationInfo?.loadLabel(packageManager).toString(),
                icon = packageInfo.applicationInfo?.loadIcon(packageManager) // Drawable
            )
        }
    }

    override suspend fun getAppDetails(packageName: String): AppDetails = withContext(Dispatchers.IO) {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val appInfo = packageInfo.applicationInfo
            ?: throw IllegalStateException("ApplicationInfo is null for package: $packageName")
        val appName = appInfo.loadLabel(packageManager).toString()
        val version = packageInfo.versionName ?: "N/A"
        val apkPath = appInfo.sourceDir
        val apkSha256 = computeSha256(File(apkPath))

        AppDetails(
            packageName = packageName,
            appName = appName,
            version = version,
            apkSha256 = apkSha256
        )
    }

    private fun computeSha256(file: File): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            file.inputStream().use { input ->
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    digest.update(buffer, 0, bytesRead)
                }
            }
            digest.digest().joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            "Error computing SHA-256"
        }
    }
}