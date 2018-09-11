package com.fedorowiat.configuration

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
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

fun zonedDate(date: Date) = date.toInstant().atZone(ZoneId.of("CET"))!!
fun timeNow() = zonedDate(Date())
fun formattedTime(zonedDateTime: ZonedDateTime) = "${zonedDateTime.hour}-${zonedDateTime.minute}"
fun formattedDate(zonedDateTime: ZonedDateTime) = "${zonedDateTime.year}-${zonedDateTime.month}-${zonedDateTime.dayOfMonth}"
fun dateFormat() = "yyyy-mm-dd"