package com.fedorowiat

import com.fedorowiat.playlist.PlaylistTask
import org.junit.Test
import kotlin.test.assertTrue

class TaskTest {
    @Test fun shouldCreatePlaylistTask() {
        val id = "fakeId"
        val playlistTask = PlaylistTask(id)

        assertTrue {
            playlistTask.playlistId === id && playlistTask is Task
        }
    }
}
