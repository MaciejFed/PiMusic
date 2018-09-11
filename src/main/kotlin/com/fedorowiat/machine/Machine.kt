package com.fedorowiat.machine

import org.springframework.data.repository.CrudRepository
import javax.persistence.*


@Entity
@Table(name = "machine")
data class Machine(
        @Id @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0L,
        val name: String = "",
        val ip: String = ""
)

interface MachineRepository: CrudRepository<Machine, Long>