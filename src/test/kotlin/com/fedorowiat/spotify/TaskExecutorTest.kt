package com.fedorowiat.spotify

import com.fedorowiat.sleep.Sleep
import com.fedorowiat.sleep.SleepRepository
import com.fedorowiat.sleep.SleepTimeService
import com.fedorowiat.wake.Wake
import com.fedorowiat.wake.WakeRepository
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import java.util.*

val sleepList = listOf(
        Sleep(date = "2018-09-08", time = "23:31", timestamp = Date(1536449460)),
        Sleep(date = "2018-09-09", time = "22:51", timestamp = Date(1536533460)),
        Sleep(date = "2018-09-10", time = "23:55", timestamp = Date(1536623700))
)

val wakeList = listOf(
        Wake(date = "2018-09-09", time = "7:10", timestamp = Date(1536477000)),
        Wake(date = "2018-09-10", time = "6:50", timestamp = Date(1536562200)),
        Wake(date = "2018-09-11", time = "7:30", timestamp = Date(1536651000))
)

val mockSleepRepository: SleepRepository = mock {
    on { findAll() }.doReturn(sleepList)
}

val mockWakeRepository: WakeRepository = mock {
    on { findAll() }.doReturn(wakeList)
}

class TaskExecutorTest {
    private val sleepTimeService = SleepTimeService(mockWakeRepository, mockSleepRepository)

    @Test fun foo() {
        sleepTimeService.listSleepTimes().forEach { println(it) }
    }
}
