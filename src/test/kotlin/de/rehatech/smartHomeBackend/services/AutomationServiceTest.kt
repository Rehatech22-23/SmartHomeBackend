package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.databuild.RoutineBuild
import de.rehatech.smartHomeBackend.repositories.RoutineRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AutomationServiceTest {

    @Autowired
    lateinit var  automationService: AutomationService

    @Autowired
    lateinit var routineService: RoutineService

    @Autowired
    lateinit var routineRepository: RoutineRepository

    var routineBuild = RoutineBuild()


    @Test
    fun testErrorRoutine()
    {
        val mockRoutine = routineService.createRoutine(routineBuild.noRangeRoutine())
        automationService.automation()
        assertEquals(1, routineRepository.count())
    }


}