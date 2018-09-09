package com.fedorowiat.spotify

import com.fedorowiat.playlist.Playlist
import com.fedorowiat.task.PlaylistTask
import org.junit.Test

class SpotifyExecutorTest {
    private val spotifyExecutor = SpotifyExecutor(SpotifyClient())

    @Test fun execute() {
        kotlin.test.assertTrue {
            spotifyExecutor.execute(PlaylistTask(Playlist.WAKE_UP_SONGS)) is Unit
        }
    }
}