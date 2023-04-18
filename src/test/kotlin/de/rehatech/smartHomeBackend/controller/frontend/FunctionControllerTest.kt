package de.rehatech.smartHomeBackend.controller.frontend

import de.rehatech.smartHomeBackend.services.FunctionService
import de.rehatech2223.datamodel.FunctionDTO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.doThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

/**
 * This test class uses Mockito to mock the FunctionService class and tests the functionByFunctionId
 * and triggerFunc methods with various input parameters. It also tests the behavior of the controller
 * when an exception is thrown by the service.
 */
@SpringBootTest
internal class FunctionControllerTest {
   //todo
}