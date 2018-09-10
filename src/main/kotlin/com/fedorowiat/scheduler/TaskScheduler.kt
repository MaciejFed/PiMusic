package com.fedorowiat.scheduler

import com.fedorowiat.configuration.ConfigurationService
import com.fedorowiat.configuration.timeNow
import com.fedorowiat.machine.Machine
import com.fedorowiat.machine.MachineRepository
import com.fedorowiat.playlist.Playlist
import com.fedorowiat.task.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.net.InetAddress

@Component
class TaskScheduler(
        private val taskExecutor: TaskExecutor,
        private val configurationService: ConfigurationService,
        private val machineRepository: MachineRepository
) {

    private var hostFailedPingMap: MutableMap<Machine, Int> = machinesToPingMap()
    private var hostSuccessPingMap: MutableMap<Machine, Int> = machinesToPingMap()
    private var nightTime = false
    private var finalSleepTime = "00:00"

    @Scheduled(fixedDelay = 2000)
    fun scheduleSleepMusic() {
        if ((timeNow().hour > afterHour() || timeNow().hour < beforeHour()) && !nightTime) {
            machineRepository.findAll().forEach {
                hostFailedPingMap[it] = if(isReachable(it)) 0 else hostFailedPingMap[it]!! + 1
            }
            if (hostFailedPingMap.keys.all { hostFailedPingMap[it]!! > 5 }) {
                nightTime = true
                hostFailedPingMap = machinesToPingMap()
                taskExecutor.execute(PlaylistTask(Playlist.SLEEP_SONGS))
                finalSleepTime = hourMinuteNowString()
            }
        }
    }

    @Scheduled(initialDelay = 50, fixedDelay = 2000)
    fun interruptSleepMusic() {
        if ((timeNow().hour > afterHour() || timeNow().hour < beforeHour()) && nightTime) {
            machineRepository.findAll().forEach {
                hostSuccessPingMap[it] = if(isReachable(it)) hostSuccessPingMap[it]!! + 1 else 0
            }
            if (hostSuccessPingMap.keys.any { hostSuccessPingMap[it]!! > 5 }) {
                nightTime = false
                hostSuccessPingMap = machinesToPingMap()
                taskExecutor.execute(StopTask())
            }
        }
    }

    @Scheduled(fixedDelay = 30000)
    fun saveSleepTime() {
        taskExecutor.execute(SaveSleepTimeTask(finalSleepTime))
    }

    @Scheduled(fixedDelay = 60000)
    fun saveWakeTime() {
        if (timeNow().hour in 5..11) {
            taskExecutor.execute(SaveWakeTimeTask(hourMinuteNowString()))
        }
    }

    private fun machinesToPingMap() = machineRepository.findAll().map { it to 0 }.toMap().toMutableMap()
    private fun afterHour() = configurationService.getConfiguration().afterHour
    private fun beforeHour() = configurationService.getConfiguration().beforeHour
    private fun hourMinuteNowString() = "${timeNow().hour}:${timeNow().minute}"
    private fun isReachable(machine: Machine): Boolean {
        return try {
            InetAddress.getByName(machine.ip).isReachable(500)
        } catch (e: Exception) {
            false
        }
    }
}