package com.example.musicapp

// Top-level response object from the Deezer API search
data class MyData(
    // FIX: 'data' must be a list of the track items
    val data: List<Track>,
    val total: Int,
    val next: String
)

// FIX: Define the Track class
data class Track(
    val id: Long,
    val title: String,        // Resolves 'title'
    val preview: String,      // Resolves 'preview'
    val duration: Int,
    val rank: Int,
    val album: Album,         // Resolves 'album'
    val artist: Artist
)

// FIX: Define the Album class
data class Album(
    val id: Long,
    val title: String,
    val cover: String         // Resolves 'cover' (used by Picasso)
)

// Define Artist if needed
data class Artist(
    val id: Long,
    val name: String
    // ...
)