package com.fedorowiat.task

import com.fedorowiat.playlist.Playlist

interface Task

data class PlaylistTask(val playlist: Playlist): Task
data class SaveSleepTimeTask(val sleepTime: String): Task
data class SaveWakeTimeTask(val wakeTime: String): Task
class StopTask: Task