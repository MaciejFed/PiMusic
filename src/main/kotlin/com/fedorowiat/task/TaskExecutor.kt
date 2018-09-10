package com.fedorowiat.task

import com.fedorowiat.configuration.timeNow
import com.fedorowiat.sleep.SleepService
import com.fedorowiat.spotify.SpotifyClient
import org.springframework.stereotype.Component

@Component
class TaskExecutor(private val spotifyClient: SpotifyClient, private val sleepService: SleepService) {

    fun execute(task: Task) {
        when (task) {
            is PlaylistTask -> spotifyClient.startPlaylist(task.playlist)
            is StopTask -> spotifyClient.stopPlaying()
            is SaveSleepTimeTask -> {
                val now = timeNow()
                val dayOfMonth = if (now.hour in 18..23) now.monthValue else now.monthValue - 1
                sleepService.saveSleep("${now.year}-${now.monthValue}-$dayOfMonth", task.sleepTime)
            }
        }
    }
}