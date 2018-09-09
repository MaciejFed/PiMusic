package com.fedorowiat.spotify

import com.fedorowiat.playlist.PlaylistTask
import org.junit.Test

class SpotifyExecutorTest {

    @Test fun execute() {
        val spotifyExecutor = SpotifyExecutor()
        kotlin.test.assertTrue {
            spotifyExecutor.execute(PlaylistTask("null")) is Unit
        }
    }
}