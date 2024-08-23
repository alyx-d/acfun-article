package com.qt.app.core.video

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.qt.app.core.utils.ComposableLifeCycle
import com.qt.app.core.video.player.R

@Composable
fun VideoPlayer(
    videoUrls: List<String>,
    coverImage: String,
    modifier: Modifier = Modifier,
) {
    check(videoUrls.isNotEmpty())
    val context = LocalContext.current
    val player: Player by remember {
        mutableStateOf(initPlayer(context, videoUrls))
    }
    val playerView = createPlayerView(player)
    ComposableLifeCycle { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                playerView.apply {
                    onResume()
                    visibility = View.VISIBLE
                }
            }

            Lifecycle.Event.ON_PAUSE -> {
                playerView.onPause()
                player.pause()
            }

            Lifecycle.Event.ON_STOP -> {
                playerView.apply {
                    onPause()
                    visibility = View.INVISIBLE
                }
            }

            else -> {}
        }
    }
    var coverImageVisible by remember { mutableStateOf(true) }
    Box(modifier = modifier) {
        AnimatedVisibility(
            enter = fadeIn(),
            exit = fadeOut(),
            visible = !coverImageVisible
        ) {
            Box {
                AndroidView(
                    modifier = modifier
                        .background(Color.Black)
                        .fillMaxWidth(),
                    factory = {
                        playerView.apply {
                            layoutParams = FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                            )
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(10.dp)
                ) {
                    // ControllerView(player)
                }
            }
        }
        AnimatedVisibility(
            enter = fadeIn(),
            exit = fadeOut(),
            visible = coverImageVisible
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        coverImageVisible = false
                        player.play()
                    },
                model = coverImage,
                contentDescription = "coverImage",
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}

private fun initPlayer(context: Context, urls: List<String>): ExoPlayer {
    return ExoPlayer.Builder(context).build().apply {
        urls.forEach { url -> addMediaItem(MediaItem.fromUri(url)) }
        playWhenReady = false
        prepare()
    }
}


@Composable
private fun createPlayerView(player: Player): PlayerView {
    val context = LocalContext.current
    val playerView by remember {
        mutableStateOf(PlayerView(context).apply {
            this.player = player
            this.keepScreenOn = true
        })
    }
    DisposableEffect(player) {
        onDispose {
            playerView.player?.release()
            playerView.player = null
        }
    }
    return playerView
}

@Composable
fun ControllerView(player: Player) {
    var isPlaying by remember { mutableStateOf(player.isPlaying) }
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        when (isPlaying) {
            true -> Icon(
                modifier = Modifier
                    .clickable {
                        player.pause()
                        isPlaying = false
                    },
                tint = Color.White,
                painter = painterResource(R.drawable.pause_24px), contentDescription = "pause"
            )

            false -> Icon(
                modifier = Modifier
                    .clickable {
                        player.play()
                        isPlaying = true
                    },
                tint = Color.White,
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "play"
            )
        }
    }
}