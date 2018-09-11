package com.fedorowiat.task

import com.fedorowiat.configuration.timeNow
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
            is SaveWakeTimeTask -> wakeService.saveWake(
                    date = "${timeNow().year}-${timeNow().monthValue}-${timeNow().dayOfMonth}",
                    time = task.wakeTime
            )
            is SaveSleepTimeTask -> {
                val now = timeNow()
                val dayOfMonth = if (now.hour in 18..23) now.dayOfMonth else now.dayOfMonth - 1
                sleepService.saveSleep(
                        date = "${now.year}-${now.monthValue}-$dayOfMonth",
                        time = task.sleepTime
                )
            }
        }
    }
}