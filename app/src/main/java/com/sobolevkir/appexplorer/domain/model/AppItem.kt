package com.sobolevkir.appexplorer.domain.model

data class AppItem(
    val packageName: String,
    val appName: String,
    val icon: Any? = null // Можно заменить на тип Drawable, если требуется
)