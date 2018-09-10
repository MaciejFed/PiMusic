package com.fedorowiat.task

import com.fasterxml.jackson.databind.ObjectMapper
import com.fedorowiat.spotify.SpotifyClient
import org.springframework.stereotype.Component
import java.io.File
import java.time.LocalDateTime

@Component
class TaskExecutor(private val spotifyClient: SpotifyClient) {

    fun execute(task: Task) {
        when (task) {
            is PlaylistTask -> spotifyClient.startPlaylist(task.playlist)
            is StopTask -> spotifyClient.stopPlaying()
            is SaveSleepTimeTask -> {
                val now = LocalDateTime.now()
                if (now.hour in 4..5) {
                    ObjectMapper().writeValue(File("/tmp/sleep-time-${now.year}-${now.month}-${now.dayOfMonth-1}"), task.sleepTime)
                }
            }
        }
    }
}