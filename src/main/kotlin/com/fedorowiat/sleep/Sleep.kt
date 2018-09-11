package com.fedorowiat.sleep

import com.fedorowiat.configuration.formattedTime
import org.springframework.data.repository.CrudRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.ZoneId
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
    fun findByDate(date: String): Sleep?
}

@Service
class SleepService(private val sleepRepository: SleepRepository) {
    fun saveSleep(timestamp: Date) {
        val dateTime = LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault())
        val dateStringFormatted = dateStringFormatted(dateTime)
        val time = formattedTime(dateTime)

        val sleepMaybe = sleepRepository.findByDate(dateStringFormatted)
        val sleepUpdate = sleepMaybe?.copy(time = time, timestamp = timestamp) ?: Sleep(date = dateStringFormatted, time = time, timestamp = timestamp)

        sleepRepository.save(sleepUpdate)
    }

    private fun dateStringFormatted(dateTime: LocalDateTime): String {
        val dateTimeCorrected = if (dateTime.hour in 18..24) dateTime else dateTime.minusDays(1)
        return dateTimeCorrected.toLocalDate().toString()
    }
}

@RestController
class SleepController(private val sleepTimeService: SleepTimeService) {
    @RequestMapping(method = [RequestMethod.GET], path = ["/sleep/time"])
    fun getAverage(): ResponseEntity<List<Pair<String, Double>>> {
        return ResponseEntity.ok(sleepTimeService.listSleepTimes())
    }
}