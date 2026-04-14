package com.example.spotify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.spotify.data.SampleData
import com.example.spotify.navigation.Screen
import com.example.spotify.ui.theme.*

@Composable
fun SearchScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }

    val filteredSongs = remember(query) {
        if (query.isEmpty()) emptyList()
        else {
            SampleData.songs.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.artist.contains(query, ignoreCase = true)
            }
        }
    }

    val categories = listOf(
        "Pop" to Color(0xFF1E3264), "Hip-Hop" to Color(0xFF8D67AB),
        "Rock" to Color(0xFFE61E32), "Electronic" to Color(0xFF0D73EC),
        "R&B" to Color(0xFFBA5D07), "Jazz" to Color(0xFF477D95),
        "Classical" to Color(0xFF148A08), "Country" to Color(0xFFAF2896),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotifyBlack)
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text("Search", color = SpotifyWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Artists, songs, or podcasts", color = SpotifyLightGray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = SpotifyBlack) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SpotifyWhite,
                unfocusedContainerColor = SpotifyWhite,
                focusedTextColor = SpotifyBlack,
                unfocusedTextColor = SpotifyBlack,
                cursorColor = SpotifyGreen
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (query.isEmpty()) {
            Text("Browse Categories", color = SpotifyWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(categories) { (name, color) ->
                    Box(
                        modifier = Modifier
                            .height(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(color)
                            .padding(12.dp)
                    ) {
                        Text(name, color = SpotifyWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        } else {
            // Search Results List
            Text("Top Results", color = SpotifyWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(filteredSongs) { song ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { navController.navigate(Screen.Player.route) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = song.albumArt,
                            contentDescription = null,
                            modifier = Modifier.size(50.dp).clip(RoundedCornerShape(4.dp)),
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
}