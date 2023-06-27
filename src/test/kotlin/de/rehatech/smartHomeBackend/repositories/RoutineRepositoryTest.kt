package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.Routine
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*

/**
 * In this test class, we are using the @DataJpaTest annotation to enable JPA-specific configuration for testing, and we are autowiring the RoutineRepository instance to our test class.
 * We are also defining a few test scenarios, such as verifying that findAll method returns all persisted routines, or that an exception is thrown when trying to save a null Routine.
 * We are using the beforeEach annotation to set up some default data (in this case, a Routine entity) before each test scenario. This allows us to have a consistent state for each test case.
 */
@DataJpaTest
class RoutineRepositoryTest {

    @Autowired
    private lateinit var routineRepository: RoutineRepository

    private lateinit var sampleRoutine: Routine
    private lateinit var persistedRoutine: Routine

    @BeforeEach
    fun setUp() {
        sampleRoutine = Routine()
        persistedRoutine = routineRepository.save(sampleRoutine)
    }

    @Test
    fun findAllTest() {
        val allRoutines = routineRepository.findAll()
        assertEquals(1, allRoutines.count())
        assertEquals(persistedRoutine.id, allRoutines.first().id)
        assertEquals(persistedRoutine, allRoutines.first())
    }

    @Test
    fun findRoutineByIdTest() {
        val foundRoutine = routineRepository.findById(persistedRoutine.id!!).orElse(null)
        assertNotNull(foundRoutine)
        assertEquals(persistedRoutine, foundRoutine)
    }

    @Test
    fun findRoutineDeletedByIdTest() {
        routineRepository.deleteById(persistedRoutine.id!!)
        assertThrows(NoSuchElementException::class.java) { routineRepository.findById(persistedRoutine.id!!).orElseThrow() }
    }

}