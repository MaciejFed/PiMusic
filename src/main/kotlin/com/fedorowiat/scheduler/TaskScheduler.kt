package com.fedorowiat.scheduler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fedorowiat.machine.Machine
import com.fedorowiat.playlist.Playlist
import com.fedorowiat.spotify.SpotifyExecutor
import com.fedorowiat.task.PlaylistTask
import com.fedorowiat.task.StopTask
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.io.File
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

@Component
class TaskScheduler(private val spotifyExecutor: SpotifyExecutor) {

    private var hostFailedPingMap: MutableMap<Machine, Int> = machinesToPingMap()
    private var hostSuccessPingMap: MutableMap<Machine, Int> = machinesToPingMap()
    private var nightTime = false
    private var finalSleepTime = "00:00"

    @Scheduled(fixedDelay = 100)
    fun scheduleSleepMusic() {
        if ((LocalDateTime.now().hour > 22 || LocalDateTime.now().hour < 4) && !nightTime) {
            Machine.values().forEach {
                hostFailedPingMap[it] = if(isReachable(it)) 0 else hostFailedPingMap[it]!! + 1
            }
            if (hostFailedPingMap.keys.all { hostFailedPingMap[it]!! > 5 }) {
                nightTime = true
                hostFailedPingMap = machinesToPingMap()
                spotifyExecutor.execute(PlaylistTask(Playlist.SLEEP_SONGS))
                finalSleepTime = SimpleDateFormat("HH:mm").format(Date())
            }
        }
    }

    @Scheduled(initialDelay = 50, fixedDelay = 100)
    fun interruptSleepMusic() {
        if ((LocalDateTime.now().hour > 22 || LocalDateTime.now().hour < 4) && nightTime) {
            Machine.values().forEach {
                hostSuccessPingMap[it] = if(isReachable(it)) hostSuccessPingMap[it]!! + 1 else 0
            }
            if (hostSuccessPingMap.keys.any { hostSuccessPingMap[it]!! > 5 }) {
                nightTime = false
                hostSuccessPingMap = machinesToPingMap()
                spotifyExecutor.execute(StopTask())
            }
        }
    }

    @Scheduled(fixedDelay = 1000)
    fun saveSleepTime() {
        val now = LocalDateTime.now()
        if (now.hour in 4..5) {
            ObjectMapper().writeValue(File("/tmp/sleep-time-${now.year}-${now.month}-${now.dayOfMonth-1}"), finalSleepTime)
        }
    }

    private fun machinesToPingMap() = Machine.values().map { it to 0 }.toMap().toMutableMap()
    private fun isReachable(machine: Machine): Boolean {
        return try {
            InetAddress.getByName(machine.ip).isReachable(500)
        } catch (e: Exception) {
            false
        }
    }
}