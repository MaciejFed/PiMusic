package com.fedorowiat.wake

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.persistence.*

@Entity
@Table(name = "wake")
data class Wake(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L,
        val date: String = "",
        val time: String = ""
)

@Repository
interface WakeRepository: CrudRepository<Wake, Long> {
    fun findByDate(date: String): Wake?
}

@Service
class WakeService(private val wakeRepository: WakeRepository) {
    fun saveWake(date: String, time: String) {
        val wakeMaybe = wakeRepository.findByDate(date)
        if(wakeMaybe == null) {
            wakeRepository.save(Wake(
                    date = date,
                    time = time
            ))
        }
    }
}
