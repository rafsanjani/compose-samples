package com.example.composesamples.components.gallery

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.systemBarsPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GallerySample() {
    var doNotShowRationale by rememberSaveable { mutableStateOf(false) }
    val storagePermissionState =
        rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    val context = LocalContext.current


    ProvideWindowInsets {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            PermissionRequired(
                permissionState = storagePermissionState,
                permissionNotGrantedContent = {
                    PermissionDenied(
                        modifier = Modifier
                            .padding(10.dp),
                        storagePermissionState = storagePermissionState,
                        onDoNotShowShowRationale = {
                            doNotShowRationale = true
                        },
                        doNotShowRationale = doNotShowRationale
                    )
                },
                permissionNotAvailableContent = {
                    PermissionNotAvailable(
                        modifier = Modifier.fillMaxSize(),
                        onOpenSettings = {
                            context.startActivity(Intent(Settings.ACTION_APPLICATION_SETTINGS))
                        })
                }
            ) {
                PermissionGranted()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, coil.annotation.ExperimentalCoilApi::class)
@Composable
private fun PermissionGranted(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val images by getAllImages(context).collectAsState(initial = emptyList())

    if (images.isEmpty()) {
        Text(modifier = modifier, text = "Loading Images")
    } else {
        ImageCarousel(images = images.sortedBy { it.getMediaId() })
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ImageCarousel(
    modifier: Modifier = Modifier,
    images: List<GalleryImage>,
) {

    var selectedImage by remember {
        mutableStateOf(images.first())
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp)
                .clip(MaterialTheme.shapes.medium)
        ) {
            AnimatedContent(
                targetState = selectedImage,
                transitionSpec = {
                    if (targetState.getMediaId() > initialState.getMediaId()) {

                        slideInHorizontally({ width -> width }) + fadeIn() with
                                slideOutHorizontally({ width -> -width }) + fadeOut()
                    } else {

                        slideInHorizontally({ width -> -width }) + fadeIn() with
                                slideOutHorizontally({ width -> width }) + fadeOut()
                    }.using(
                        SizeTransform(clip = false)
                    )
                }
            ) { targetGalleryImage ->
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    data = targetGalleryImage.uri,
                    contentScale = ContentScale.Crop
                )
            }

        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(150.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(images, key = { it.uri }) { galleryImage ->
                ImageCard(
                    modifier = Modifier
                        .height(170.dp)
                        .width(120.dp),
                    image = galleryImage,
                    selected = selectedImage == galleryImage
                ) {
                    selectedImage = it
                }
            }
        }
    }
}

@Composable
private fun ImageCard(
    modifier: Modifier = Modifier,
    image: GalleryImage,
    selected: Boolean = false,
    onClick: (image: GalleryImage) -> Unit = {},
) {
    val border = Modifier.border(
        width = 5.dp,
        color = Color.Blue.copy(alpha = 0.65f),
        shape = MaterialTheme.shapes.medium
    )

    Card(
        modifier = modifier
            .then(if (selected) border else Modifier)
            .clickable {
                onClick(image)
            }) {
        Image(
            data = image.uri,
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
private fun PermissionNotAvailable(
    modifier: Modifier = Modifier,
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Storage permission denied. Please grant storage permission from your device settings to proceed",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.subtitle2
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                onOpenSettings()
            }, modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Open Settings")
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionDenied(
    modifier: Modifier = Modifier,
    storagePermissionState: PermissionState,
    doNotShowRationale: Boolean,
    onDoNotShowShowRationale: () -> Unit
) {
    if (doNotShowRationale) {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = "Unable to launch sample without storage permission!",
            textAlign = TextAlign.Center
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Storage Permission required",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = " Please be a good neighbour and allow it so that we can proceed in peace!",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body2
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = { storagePermissionState.launchPermissionRequest() }) {
                    Text("Ok")
                }

                Spacer(Modifier.width(8.dp))

                TextButton(onClick = { onDoNotShowShowRationale() }) {
                    Text("Nope")
                }
            }
        }
    }
}


private fun getAllImages(context: Context): Flow<List<GalleryImage>> = flow {
    val uriExternal: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val projection = arrayOf(MediaStore.Images.Media._ID)

    var imageId: Long

    val cursor = context.contentResolver.query(
        uriExternal,
        projection,
        null,
        null,
        null
    )

    cursor?.use {
        val columnIndexID = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

        val listOfUri = mutableListOf<GalleryImage>()

        while (cursor.moveToNext()) {
            imageId = cursor.getLong(columnIndexID)
            val uriImage = Uri.withAppendedPath(uriExternal, "$imageId")
            listOfUri.add(GalleryImage(uri = uriImage))
        }

        emit(listOfUri)
    }

}.flowOn(Dispatchers.IO)