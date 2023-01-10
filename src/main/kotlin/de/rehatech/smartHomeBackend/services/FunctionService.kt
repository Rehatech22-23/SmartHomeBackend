package de.rehatech.smartHomeBackend.services

import datamodel.function
import de.rehatech.smartHomeBackend.Enum.FunctionType
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.Item
import de.rehatech.smartHomeBackend.repositories.FunctionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import de.rehatech.smartHomeBackend.entities.FunctionValues
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository


@Service
class FunctionService  @Autowired constructor(
    val functionRepository: FunctionRepository,
    val openHabRepository: OpenHabRepository,
) {

    fun saveFunctionOpenHab(uid:String, item: Item)
    {
        val openhab = openHabRepository.findOpenHabByUid(uid)
        val functType = functionsTypeOpenHab(item)
        if(functType != null)
        {
            val newFunctionValues = FunctionValues(label = item.label, name = item.name, type = functType , deviceOpenHab = openhab )
            functionRepository.save(newFunctionValues)
        }

    }

    private fun functionsTypeOpenHab(item: Item): FunctionType?
    {
        var itemstring = item.type

        if (itemstring.contains(":"))
        {
            val erg= itemstring.split(":")
            itemstring = erg[0]
        }
        if(itemstring.contains("String"))
        {
            itemstring = "StringType"
        }
        try {
            val test =FunctionType.valueOf(itemstring)
            return test
        }
        catch (  e:IllegalArgumentException      )
        {
            return null
        }



    }

    fun getFunctionFromItem(item: Item, functionValue: FunctionValues):function?
    {
        val functionType = functionsTypeOpenHab(item) ?: return null
        when(functionType){
            FunctionType.Switch -> {
                var on = false
                if(item.state == "ON")
                {
                    on = true
                }
                return  function(functionName = functionValue.name, functionId = functionValue.id!!, onOff = on, outputValue = item.state )
            }
            // ToDo return null durch die richtige umwandkung ersetzen
            FunctionType.Color -> return null
            FunctionType.Call -> return null
            FunctionType.Contact -> return null
            FunctionType.Datetime -> return null
            FunctionType.Dimmer -> return null
            FunctionType.Group -> return null
            FunctionType.Image -> return null
            FunctionType.Location -> return null
            FunctionType.Number -> return  function(functionName = functionValue.name, functionId = functionValue.id!!, outputValue = item.state )
            FunctionType.Player -> return null
            FunctionType.Rollershutter -> return null
            FunctionType.StringType -> return  function(functionName = functionValue.name, functionId = functionValue.id!!, outputValue = item.state )
        }

    }
}