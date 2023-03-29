package de.rehatech.smartHomeBackend.repositories

import de.rehatech.smartHomeBackend.entities.HomeeDevice
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.dao.EmptyResultDataAccessException
import java.util.*


/**
 * In this test class, we are using the @DataJpaTest annotation to enable JPA-specific configuration for testing, and we are autowiring the HomeeDeviceRepository instance to our test class.
 * We are also defining a few test scenarios, such as verifying that findHomeeByHomeeID method returns a valid HomeeDevice entity when a matching homeeID is provided, or that an exception is thrown when trying to save a null HomeeDevice.
 * We are using the beforeEach annotation to set up some default data (in this case, a HomeeDevice entity) before each test scenario. This allows us to have a consistent state for each test case.
 *
 * @author Tim Bräuker
 */
@DataJpaTest
class HomeeDeviceRepositoryTest {

    @Autowired
    private lateinit var sut: HomeeDeviceRepository

    private lateinit var device: HomeeDevice
    private lateinit var persistedDevice: HomeeDevice

    @BeforeEach
    fun setUp() {
        device = HomeeDevice(1, "Device 1",1)
        persistedDevice = sut.save(device)
    }

    /**
     * findHomeeByHomeeID should return valid HomeeDevice entity when a matching homeeID is provided
     */
    @Test
    fun findHomeeByHomeeID() {
        val result = sut.findHomeeByHomeeID(device.homeeID)
        assertEquals(persistedDevice.id, result.id)
        assertEquals(persistedDevice.homeeID, result.homeeID)
        assertEquals(persistedDevice.name, result.name)
    }

    /**
     * save should throw NullPointerException when saving a null HomeeDevice
     */
    @Test
    fun saveNull() {
        assertThrows(NullPointerException::class.java) { sut.save(null!!) }
    }

    /**
     * findById should throw NoSuchElementException when trying to retrieve a non-existing HomeeDevice by id
     */
    @Test
    fun findById() {
        assertThrows(NoSuchElementException::class.java) { sut.findById(999).get() }
    }

    /**
     * deleteById should throw NoSuchElementException when trying to delete a non-existing HomeeDevice by id
     */
    @Test
    fun deleteById() {
        assertThrows(EmptyResultDataAccessException::class.java) { sut.deleteById(999) }
    }

    /**
     * save should throw IllegalArgumentException when trying to save a HomeeDevice with a negative id
     */
    @Test
    fun save() {
        val invalidDevice = HomeeDevice(-1, "Invalid Device",1 )

        assertThrows(IllegalArgumentException::class.java) { sut.save(invalidDevice) }
    }
}