package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.TriggerTime
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.util.*


/**
 * In this test class, we are using the @DataJpaTest annotation to enable JPA-specific configuration for testing, and we are autowiring the TriggerTimeRepository instance to our test class.
 * We are also defining a few test scenarios, such as verifying that findAll method returns all persisted trigger times, or that an exception is thrown when trying to save a null TriggerTime.
 * Note that we're dealing with nullable types here (i.e. TriggerTime?), so we need to add null checks where appropriate (e.g. using the safe navigation operator !!).
 * We are using the beforeEach annotation to set up some default data (in this case, a TriggerTime entity) before each test scenario. This allows us to have a consistent state for each test case.
 */
@DataJpaTest
class TriggerTimeRepositoryTest {

    @Autowired
    private lateinit var triggerTimeRepository: TriggerTimeRepository

    private lateinit var sampleTriggerTime: TriggerTime
    private lateinit var persistedTriggerTime: TriggerTime

    @BeforeEach
    fun setUp() {
        sampleTriggerTime = TriggerTime()
        persistedTriggerTime = triggerTimeRepository.save(sampleTriggerTime)
    }

    /**
     * findAll should return all persisted trigger times
     */
    @Test
    fun findAll() {
        val allTriggerTimes = triggerTimeRepository.findAll()
        assertEquals(1, allTriggerTimes.count())
        assertEquals(persistedTriggerTime.id, allTriggerTimes.first()!!.id)
        assertEquals(persistedTriggerTime, allTriggerTimes.first())
        assertEquals(persistedTriggerTime, allTriggerTimes.first())
    }

    @Test
    fun findTriggerTimeById() {
        val foundTriggerTime = triggerTimeRepository.findById(persistedTriggerTime.id!!).orElse(null)
        assertNotNull(foundTriggerTime)
        assertEquals(persistedTriggerTime, foundTriggerTime)
    }

    @Test
    fun deleteTriggerTimeById() {
        triggerTimeRepository.deleteById(persistedTriggerTime.id!!)
        assertThrows(NoSuchElementException::class.java) { triggerTimeRepository.findById(persistedTriggerTime.id!!).orElseThrow() }
    }

    /**
     * save should throw NullPointerException when saving a null TriggerTime
     */
    @Test
    fun save() {
        assertThrows(NullPointerException::class.java) { triggerTimeRepository.save(null!!) }
    }
}