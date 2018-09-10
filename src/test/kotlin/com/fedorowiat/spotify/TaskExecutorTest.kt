package com.fedorowiat.spotify

import com.fedorowiat.playlist.Playlist
import com.fedorowiat.task.PlaylistTask
import com.fedorowiat.task.TaskExecutor
import org.junit.Test

class TaskExecutorTest {
    private val spotifyExecutor = TaskExecutor(SpotifyClient())

    @Test fun execute() {
        kotlin.test.assertTrue {
            spotifyExecutor.execute(PlaylistTask(Playlist.WAKE_UP_SONGS)) is Unit
        }
    }
}