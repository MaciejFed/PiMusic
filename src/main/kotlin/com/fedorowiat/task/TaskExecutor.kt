package com.fedorowiat.task

import com.fedorowiat.sleep.SleepService
import com.fedorowiat.spotify.SpotifyClient
import com.fedorowiat.wake.WakeService
import org.springframework.stereotype.Component

@Component
class TaskExecutor(
        private val spotifyClient: SpotifyClient,
        private val sleepService: SleepService,
        private val wakeService: WakeService
) {

    fun execute(task: Task) {
        when (task) {
            is PlaylistTask -> spotifyClient.startPlaylist(task.playlist)
            is StopTask -> spotifyClient.stopPlaying()
            is SaveWakeTimeTask -> wakeService.saveWake(task.wakeTime)
            is SaveSleepTimeTask -> sleepService.saveSleep(task.sleepTime)
        }
    }
}