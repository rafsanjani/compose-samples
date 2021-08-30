package com.example.composesamples.components.gallery

import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

@Composable
fun Image(
    modifier: Modifier = Modifier,
    data: Uri,
    contentScale: ContentScale = ContentScale.FillBounds
) {
    val context = LocalContext.current

    val glideRequest = remember {
        Glide.with(context)
    }


    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    val target: CustomTarget<Drawable> = object : CustomTarget<Drawable>() {
        override fun onLoadCleared(placeholder: Drawable?) {
            //NO-OP
        }

        override fun onResourceReady(
            resource: Drawable,
            transition: Transition<in Drawable>?
        ) {
            imageBitmap = resource.toBitmap().asImageBitmap()
        }
    }

    glideRequest.load(data)
        .into(target)

    if (imageBitmap == null) {
        Box(
            modifier = modifier
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            Color.Gray,
                            Color.Gray.copy(alpha = 0.5f)
                        )
                    )
                )
                .fillMaxHeight()
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        Image(
            bitmap = imageBitmap!!,
            contentDescription = null,
            contentScale = contentScale,
            modifier = modifier
        )
    }

}