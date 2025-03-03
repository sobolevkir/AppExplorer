package com.sobolevkir.appexplorer.data

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.sobolevkir.appexplorer.domain.AppsRepository
import com.sobolevkir.appexplorer.domain.model.AppDetails
import com.sobolevkir.appexplorer.domain.model.AppItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import javax.inject.Inject

class AppsRepositoryImpl @Inject constructor(private val context: Context) : AppsRepository {

    private val packageManager: PackageManager
        get() = context.packageManager

    override suspend fun getInstalledApps(): List<AppItem> = withContext(Dispatchers.IO) {
        val installedPackages = packageManager.getInstalledPackages(0)
        installedPackages.map {
            val packageName = it.packageName
            val iconUri = it.applicationInfo?.loadIcon(packageManager)
                ?.let { drawableIcon -> getDrawableUri(drawableIcon, packageName) }
            AppItem(
                packageName = packageName,
                appName = it.applicationInfo?.loadLabel(packageManager).toString(),
                appIconUri = iconUri
            )
        }
    }

    override suspend fun getAppDetails(packageName: String): AppDetails =
        withContext(Dispatchers.IO) {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val appInfo = packageInfo.applicationInfo
                ?: throw IllegalStateException("ApplicationInfo is null for package: $packageName")
            val apkSha256 = computeSha256(File(appInfo.sourceDir))
            val iconUri = appInfo.loadIcon(packageManager)
                ?.let { drawableIcon -> getDrawableUri(drawableIcon, packageName) }
            AppDetails(
                packageName = packageName,
                appName = appInfo.loadLabel(packageManager).toString(),
                version = packageInfo.versionName ?: "N/A",
                apkSha256 = apkSha256,
                iconStringUri = iconUri
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

    private fun getDrawableUri(drawable: Drawable, packageName: String): String? {
        val cachedFile = File(context.cacheDir, "icon_${packageName}.png")
        if (cachedFile.exists()) {
            return cachedFile.absolutePath
        }
        return try {
            val bitmap = when (drawable) {
                is BitmapDrawable -> drawable.bitmap
                else -> {
                    val bitmap = Bitmap.createBitmap(
                        drawable.intrinsicWidth,
                        drawable.intrinsicHeight,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)
                    bitmap
                }
            }
            bitmap?.let {
                val outputStream = FileOutputStream(cachedFile)
                it.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
                cachedFile.absolutePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}