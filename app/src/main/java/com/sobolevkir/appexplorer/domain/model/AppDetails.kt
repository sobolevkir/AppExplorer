package com.sobolevkir.appexplorer.domain.model

data class AppDetails(
    val packageName: String,
    val appName: String,
    val version: String?,
    val apkSha256: String?,
)