package com.sobolevkir.appexplorer.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import com.sobolevkir.appexplorer.domain.api.AppsRepository
import com.sobolevkir.appexplorer.domain.model.AppDetails
import com.sobolevkir.appexplorer.domain.model.AppItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import javax.inject.Inject

class AppsRepositoryImpl @Inject constructor(private val context: Context) : AppsRepository {

    private val packageManager: PackageManager
        get() = context.packageManager

    override fun getInstalledAppsFlow(): Flow<List<AppItem>> = callbackFlow {
        val sendAppsList = { trySend(loadInstalledApps()) }
        val packageReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                sendAppsList()
            }
        }
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addDataScheme("package")
        }
        context.registerReceiver(packageReceiver, filter)
        sendAppsList()
        awaitClose { context.unregisterReceiver(packageReceiver) }
    }.flowOn(Dispatchers.IO)

    override suspend fun getAppDetails(packageName: String): AppDetails? =
        withContext(Dispatchers.IO) {
            try {
                val packageInfo =
                    packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
                val appInfo = packageInfo.applicationInfo
                    ?: throw IllegalStateException("applicationInfo == null для пакета: $packageName")
                val iconUri = appInfo.loadIcon(packageManager)
                    ?.let { drawableIcon -> getDrawableUri(drawableIcon, packageName) }
                AppDetails(
                    packageName = packageName,
                    appName = appInfo.loadLabel(packageManager).toString(),
                    version = packageInfo.versionName,
                    apkSha256 = computeSha256(File(appInfo.sourceDir)),
                    iconStringUri = iconUri
                )
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e("Пакет не найден: $packageName", e.toString())
                null
            } catch (e: Exception) {
                Log.e("Ошибка при получении данных о пакете: $packageName", e.toString())
                null
            }
        }

    private fun loadInstalledApps(): List<AppItem> {
        val launchableApps = packageManager.queryIntentActivities(
            Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_LAUNCHER) }, 0
        )

        return launchableApps.mapNotNull { app ->
            val packageName = app.activityInfo.packageName
            val appInfo = try {
                packageManager.getApplicationInfo(packageName, 0)
            } catch (e: Exception) {
                null
            }
            appInfo?.let {
                val iconUri = it.loadIcon(packageManager)
                    ?.let { drawableIcon -> getDrawableUri(drawableIcon, packageName) }
                AppItem(
                    packageName = packageName,
                    appName = it.loadLabel(packageManager).toString(),
                    appIconUri = iconUri
                )
            }
        }
    }

    private fun computeSha256(file: File): String? {
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
            Log.e("Ошибка получения SHA-256 для файла: ${file.name}", e.toString())
            null
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
            Log.e("Ошибка сохранения иконки приложения: $packageName", e.toString())
            null
        }
    }

}