package com.fedorowiat.configuration

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*


@Entity
@Table(name = "configuration")
data class Configuration(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO) val id: Long = 0L,
        @Column(length = 1024) val accessToken: String = "",
        @Column(length = 1024) val refreshToken: String = "",
        @Column val afterHour: Int = 0,
        @Column val beforeHour: Int = 0
)

@Repository
interface ConfigurationRepository: CrudRepository<Configuration, Long>

@Service
class ConfigurationService(private val configurationRepository: ConfigurationRepository) {

    fun getConfiguration(): Configuration {
        return try {
            configurationRepository.findAll().single()
        } catch (e: Exception) {
            throw IllegalStateException("No configuration found!", e)
        }
    }

    fun updateAccessToken(accessToken: String) {
        val configuration = configurationRepository.findAll().single()
        configurationRepository.save(configuration.copy(
                accessToken = accessToken
        ))
    }
}

fun timeNow() = LocalDateTime.now()!!
fun formattedTime(dateTime: LocalDateTime) = "${dateTime.hour}:${dateTime.minute}"
fun formattedDate(localDate: LocalDate) = SimpleDateFormat(dateFormat()).format(localDate)!!
fun dateFormat() = "yyyy-mm-dd"