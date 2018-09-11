package com.fedorowiat.wake

import com.fedorowiat.configuration.formattedTime
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "wake")
data class Wake(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L,
        val date: String = "",
        val time: String = "",
        val timestamp: Date = Date()
)

@Repository
interface WakeRepository: CrudRepository<Wake, Long> {
    fun findByDate(date: String): Wake?
}

@Service
class WakeService(private val wakeRepository: WakeRepository) {
    fun saveWake(timestamp: Date) {
        val formattedDate = LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()).toLocalDate().toString()
        val wakeMaybe = wakeRepository.findByDate(formattedDate)

        if(wakeMaybe == null) {
            wakeRepository.save(Wake(
                    date = formattedDate,
                    time = formattedTime(LocalDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault())),
                    timestamp = timestamp
            ))
        }
    }
}
