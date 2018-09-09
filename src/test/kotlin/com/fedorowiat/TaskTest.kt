package com.fedorowiat

import com.fedorowiat.playlist.Playlist
import com.fedorowiat.playlist.PlaylistTask
import org.junit.Test
import kotlin.test.assertTrue

class TaskTest {
    @Test fun shouldCreatePlaylistTask() {
        val playlistTask = PlaylistTask(Playlist.WAKE_UP_SONGS)

        assertTrue {
            playlistTask.playlist.playlistContext === Playlist.WAKE_UP_SONGS.playlistContext && playlistTask is Task
        }
    }
}
