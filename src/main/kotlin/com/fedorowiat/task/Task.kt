package com.fedorowiat.task

import com.fedorowiat.playlist.Playlist
import java.time.ZoneId
import java.util.*


interface Task

data class PlaylistTask(val playlist: Playlist): Task
data class SaveSleepTimeTask(val sleepTime: Date): Task
data class SaveWakeTimeTask(val wakeTime: Date): Task
class StopTask: Task

fun main(args: Array<String>) {
    val now = Date()
    print(now.toInstant().atZone(ZoneId.of("UTC")))
}