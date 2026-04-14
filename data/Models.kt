package com.example.spotify.data

data class Song(
    val id: Int,
    val title: String,
    val artist: String,
    val albumArt: String,
    val musicResId: Int,
    val duration: String
)

data class Playlist(
    val id: Int,
    val name: String,
    val coverArt: String,
    val songs: List<Song>
)