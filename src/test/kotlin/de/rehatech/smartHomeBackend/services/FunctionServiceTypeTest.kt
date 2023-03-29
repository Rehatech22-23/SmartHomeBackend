package de.rehatech.smartHomeBackend.services

import de.rehatech.homeekt.model.attributes
import de.rehatech.smartHomeBackend.enums.FunctionType
import de.rehatech.smartHomeBackend.response.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest

/**
 * This test class uses Mockito to mock the attributes and Item classes, and it tests the functionsTypeHomee
 * and functionsTypeOpenHab methods with various input parameters.
 */

@SpringBootTest
class FunctionTypeServiceTest {
    private var functionTypeService: FunctionTypeService? = null
    @BeforeEach
    fun setUp() {
        functionTypeService = FunctionTypeService()
    }

    @Test
    fun testFunctionsTypeHomee() {
        val mockAttribute = Mockito.mock(attributes::class.java)
        Mockito.`when`(mockAttribute.type).thenReturn(1)
        Assertions.assertEquals(FunctionType.Switch, functionTypeService!!.functionsTypeHomee(mockAttribute))
        Mockito.`when`(mockAttribute.type).thenReturn(2)
        Assertions.assertEquals(FunctionType.Dimmer, functionTypeService!!.functionsTypeHomee(mockAttribute))

        // add more tests for other attribute types
        Mockito.`when`(mockAttribute.type).thenReturn(385)
        Assertions.assertEquals(null, functionTypeService!!.functionsTypeHomee(mockAttribute))
    }

    @Test
    fun testFunctionsTypeOpenHab() {
        val mockItem = Mockito.mock(Item::class.java)
        Mockito.`when`(mockItem.type).thenReturn("Switch")
        Assertions.assertEquals(FunctionType.Switch, functionTypeService!!.functionsTypeOpenHab(mockItem))
        Mockito.`when`(mockItem.type).thenReturn("Color")
        Assertions.assertEquals(FunctionType.Color, functionTypeService!!.functionsTypeOpenHab(mockItem))

        // add more tests for other item types
        Mockito.`when`(mockItem.type).thenReturn("UnknownType")
        Assertions.assertEquals(null, functionTypeService!!.functionsTypeOpenHab(mockItem))
    }
}