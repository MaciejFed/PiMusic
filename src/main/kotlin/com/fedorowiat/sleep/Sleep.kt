package com.fedorowiat.sleep

import com.fedorowiat.configuration.formattedDate
import com.fedorowiat.configuration.formattedTime
import com.fedorowiat.configuration.zonedDate
import org.springframework.data.repository.CrudRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "sleep")
data class Sleep(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L,
        val date: String = "",
        val time: String = "",
        val timestamp: Date = Date()
)

@Repository
interface SleepRepository: CrudRepository<Sleep, Long> {
    fun findByTimestamp(date: String): Sleep?
}

@Service
class SleepService(private val sleepRepository: SleepRepository) {
    fun saveSleep(timestamp: Date) {
        val zonedDate = zonedDate(timestamp)
        val dateStringFormatted = dateStringFormatted(zonedDate)
        val time = formattedTime(zonedDate)

        val sleepMaybe = sleepRepository.findByTimestamp(dateStringFormatted)
        val sleepUpdate = sleepMaybe?.copy(time = time, timestamp = timestamp) ?: Sleep(date = dateStringFormatted, time = time, timestamp = timestamp)

        sleepRepository.save(sleepUpdate)
    }

    private fun dateStringFormatted(zonedDate: ZonedDateTime): String {
        val zonedDateCorrected = if (zonedDate.hour in 18..24) zonedDate else zonedDate.minusDays(1)
        return formattedDate(zonedDateCorrected)
    }
}

@RestController
class SleepController(private val sleepRepository: SleepRepository) {
    @RequestMapping(method = [RequestMethod.GET], path = ["/sleep/average"])
    fun getAverage(): ResponseEntity<Double> {
        return ResponseEntity.ok(sleepRepository.findAll().map { sleep -> sleep.timestamp.time }.average())
    }
}