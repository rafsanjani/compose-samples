package com.example.composesamples.components.gallery

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap

data class GalleryImage(
    val uri: Uri,
    var bitmap: ImageBitmap? = null
) {
    fun getMediaId(): Int = uri.pathSegments[3].toInt()
}
