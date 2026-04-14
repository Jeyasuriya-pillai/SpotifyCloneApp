package com.example.spotify.ui.screens

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotify.data.SampleData
import com.example.spotify.ui.theme.*
import kotlinx.coroutines.delay

@OptIn(UnstableApi::class)
@Composable
fun PlayerScreen(navController: NavController, songId: Int) {
    val context = LocalContext.current

    val song = remember(songId) {
        SampleData.songs.find { it.id == songId } ?: SampleData.songs.first()
    }

    var isPlaying by remember { mutableStateOf(true) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var totalDuration by remember { mutableLongStateOf(0L) }

    val exoPlayer = remember(song) {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri("android.resource://${context.packageName}/${song.musicResId}")
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }

    LaunchedEffect(exoPlayer, isPlaying) {
        while (isPlaying) {
            currentPosition = exoPlayer.currentPosition
            totalDuration = exoPlayer.duration.coerceAtLeast(0L)
            delay(500) // 0.5 sec delay for smoother slider
        }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    fun formatTime(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60)) % 60
        return "%d:%02d".format(minutes, seconds)
    }

    Column(
        modifier = Modifier.fillMaxSize().background(SpotifyBlack).padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.KeyboardArrowDown, "Back", tint = Color.White, modifier = Modifier.size(32.dp))
            }
            Text("Now Playing", color = Color.White, fontWeight = FontWeight.SemiBold)
            IconButton(onClick = {}) { Icon(Icons.Filled.MoreVert, "More", tint = Color.White) }
        }

        Spacer(modifier = Modifier.height(40.dp))

        AsyncImage(
            model = song.albumArt,
            contentDescription = null,
            modifier = Modifier.size(320.dp).clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(40.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(song.title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(song.artist, color = Color.Gray, fontSize = 16.sp)
            }
            Icon(Icons.Filled.FavoriteBorder, null, tint = Color.Gray, modifier = Modifier.size(28.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        val sliderPos = if (totalDuration > 0) currentPosition.toFloat() / totalDuration.toFloat() else 0f
        Slider(
            value = sliderPos.coerceIn(0f, 1f),
            onValueChange = {
                val seekTo = (it * totalDuration).toLong()
                exoPlayer.seekTo(seekTo)
                currentPosition = seekTo
            },
            colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(formatTime(currentPosition), color = Color.Gray, fontSize = 12.sp)
            Text(formatTime(totalDuration), color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Shuffle, null, tint = Color.Gray)
            Icon(Icons.Filled.SkipPrevious, null, tint = Color.White, modifier = Modifier.size(40.dp))

            Box(
                modifier = Modifier.size(64.dp).clip(CircleShape).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {
                    if (exoPlayer.isPlaying) exoPlayer.pause() else exoPlayer.play()
                    isPlaying = exoPlayer.isPlaying
                }) {
                    Icon(if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, null, tint = Color.Black, modifier = Modifier.size(36.dp))
                }
            }

            Icon(Icons.Filled.SkipNext, null, tint = Color.White, modifier = Modifier.size(40.dp))
            Icon(Icons.Filled.Repeat, null, tint = Color.Gray)
        }
    }
}