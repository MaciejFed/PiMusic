package com.fedorowiat.spotify

import com.fedorowiat.task.PlaylistTask
import com.fedorowiat.task.StopTask
import com.fedorowiat.task.Task
import org.springframework.stereotype.Component

@Component
class SpotifyExecutor(private val spotifyClient: SpotifyClient) {
    fun execute(task: Task) {
        when (task) {
            is PlaylistTask -> spotifyClient.startPlaylist(task.playlist)
            is StopTask -> spotifyClient.stopPlaying()
        }
    }
}