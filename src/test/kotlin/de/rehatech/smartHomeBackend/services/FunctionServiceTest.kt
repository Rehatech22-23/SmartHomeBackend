package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.controller.backend.BackendController
import de.rehatech.smartHomeBackend.repositories.DeviceMethodsRepository
import de.rehatech.smartHomeBackend.repositories.HomeeDeviceRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabDeviceRepository
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FunctionServiceTest {

    @InjectMocks
    lateinit var functionService: FunctionService

    @Mock
    lateinit var backendController: BackendController

    @Mock
    lateinit var deviceMethodsRepository: DeviceMethodsRepository

    @Mock
    lateinit var openHabDeviceRepository: OpenHabDeviceRepository

    @Mock
    lateinit var homeeDeviceRepository: HomeeDeviceRepository

    @Mock
    lateinit var functionTypeService: FunctionTypeService

    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetFunction() {
        // TODO: Implement this test
    }

    @Test
    fun testTriggerFunc() {
        // TODO: Implement this test
    }

    @Test
    fun testSaveFunctionOpenHab() {
        // TODO: Implement this test
    }

    @Test
    fun testSaveFunctionHomee() {
        // TODO: Implement this test
    }
}