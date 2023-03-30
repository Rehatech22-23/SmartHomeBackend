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
    @Autowired
    private var functionController: FunctionController? = null
    @Autowired
    private var mockFunctionService: FunctionService? = null
    @BeforeEach
    fun setUp() {
        mockFunctionService = Mockito.mock(FunctionService::class.java)
    }

    @Test
    fun testFunctionByFunctionId() {
        val mockFunctionDTO = Mockito.mock(FunctionDTO::class.java)
        Mockito.`when`(mockFunctionService!!.getFunction(ArgumentMatchers.anyLong())).thenReturn(mockFunctionDTO)
        val result = functionController!!.functionByFunctionId(123L)
        Mockito.verify(mockFunctionService)!!.getFunction(123L)
    }

    @Test
    fun testFunctionByFunctionIdWhenExceptionThrown() {
        Mockito.`when`(mockFunctionService!!.getFunction(ArgumentMatchers.anyLong())).thenThrow(NullPointerException("Test exception"))
        try {
            functionController!!.functionByFunctionId(123L)
        } catch (ex: ResponseStatusException) {
            assert(ex.statusCode === HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @Test
    fun testTriggerFunc() {
        functionController!!.triggerFunc("device1", 456L, 1.23f)
        Mockito.verify(mockFunctionService)!!.triggerFunc("device1", 456L, 1.23f)
    }

    @Test
    fun testTriggerFuncWhenExceptionThrown() {
        doThrow(NullPointerException("Test exception")).`when`(mockFunctionService)!!.triggerFunc(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.anyFloat())
        try {
            functionController!!.triggerFunc("device1", 456L, 1.23f)
        } catch (ex: ResponseStatusException) {
            assert(ex.statusCode === HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}