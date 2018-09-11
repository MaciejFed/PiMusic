package com.fedorowiat.sleep

import com.fedorowiat.configuration.dateFormat
import com.fedorowiat.wake.WakeRepository
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Service
class SleepTimeService(private val wakeRepository: WakeRepository, private val sleepRepository: SleepRepository) {

    fun listSleepTimes(): List<Pair<String, Double>> {
        return sleepRepository
                .findAll()
                .sortedBy { it.timestamp }
                .takeWhile { wakeRepository.findByDate(anotherDay(it.date)) != null }
                .map { sleep -> Pair(sleep, wakeRepository.findByDate(anotherDay(sleep.date))!!) }
                .map { sleepWakePair -> Pair(sleepWakePair.first.date, (sleepWakePair.second.timestamp.time - sleepWakePair.first.timestamp.time) / 3600000.0) }
                .toList()
    }

    private fun anotherDay(date: String): String {
        val instant = SimpleDateFormat(dateFormat())
                    .parse(date)
                    .toInstant()
                    .plusMillis(TimeUnit.DAYS.toMillis(1))

        return SimpleDateFormat(dateFormat()).format(Date.from(instant))
    }
}