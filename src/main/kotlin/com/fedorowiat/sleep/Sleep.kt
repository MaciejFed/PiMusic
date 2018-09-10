package com.fedorowiat.sleep

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.persistence.*

@Entity
@Table(name = "sleep")
data class Sleep(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L,
        val date: String = "",
        val time: String = ""
)

@Repository
interface SleepRepository: CrudRepository<Sleep, Long> {
    fun findByDate(date: String): Sleep?
}

@Service
class SleepService(private val sleepRepository: SleepRepository) {
    fun saveSleep(date: String, time: String) {
        val sleepMaybe = sleepRepository.findByDate(date)
        val sleepUpdate = sleepMaybe?.copy(time = time) ?: Sleep(date = date, time = time)

        sleepRepository.save(sleepUpdate)
    }
}
