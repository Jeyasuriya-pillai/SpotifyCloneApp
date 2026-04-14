package com.example.spotify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotify.data.SampleData
import com.example.spotify.navigation.Screen
import com.example.spotify.ui.theme.*

@Composable
fun LibraryScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotifyBlack)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(SpotifyGreen),
                contentAlignment = Alignment.Center
            ) {
                Text("U", color = SpotifyBlack, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text("Your Library", color = SpotifyWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* Navigate to Liked Songs if you want */ }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(SpotifyLightGray), // Placeholder color
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = SpotifyGreen)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Liked Songs", color = SpotifyWhite, fontWeight = FontWeight.SemiBold)
                Text("${SampleData.songs.size} songs", color = SpotifyLightGray, fontSize = 14.sp)
            }
        }

        Divider(color = SpotifyDarkGray, thickness = 0.5.dp, modifier = Modifier.padding(vertical = 16.dp))

        Text("All Music", color = SpotifyWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(SampleData.songs) { song ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(Screen.Player.route) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = song.albumArt,
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(song.title, color = SpotifyWhite, fontWeight = FontWeight.Medium)
                        Text(song.artist, color = SpotifyLightGray, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}