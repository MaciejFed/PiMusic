package com.fedorowiat.spotify

import com.fedorowiat.Task
import com.fedorowiat.playlist.Playlist
import com.fedorowiat.playlist.PlaylistTask

class SpotifyExecutor(private val spotifyClient: SpotifyClient) {
    fun execute(task: Task) {
        when (task) {
            is PlaylistTask -> spotifyClient.startPlaylist(Playlist.WAKE_UP_SONGS)
        }
    }
}