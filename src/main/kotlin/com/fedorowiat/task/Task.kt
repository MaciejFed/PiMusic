package com.fedorowiat.task

import com.fedorowiat.playlist.Playlist

interface Task

data class PlaylistTask(val playlist: Playlist): Task
class StopTask: Task