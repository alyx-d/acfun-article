package com.qt.app.core.video

import android.content.Context
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@UnstableApi
@Composable
fun VideoPlayer(videoUrls: List<String>, modifier: Modifier = Modifier) {
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
    AndroidView(
        modifier = Modifier,
        factory = { playerView }
    )
}

private fun initPlayer(context: Context, urls: List<String>): ExoPlayer {
    return ExoPlayer.Builder(context).build().apply {
        urls.forEach { url -> addMediaItem(MediaItem.fromUri(url)) }
        playWhenReady = true
        prepare()
    }
}

@Composable
private fun ComposableLifeCycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@UnstableApi
@Composable
private fun createPlayerView(player: Player): PlayerView {
    val context = LocalContext.current
    val playerView by remember {
        mutableStateOf(PlayerView(context).apply {
            this.player = player
            this.keepScreenOn = true
            this.setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
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