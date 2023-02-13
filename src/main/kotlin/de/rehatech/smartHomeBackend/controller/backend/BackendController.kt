package de.rehatech.smartHomeBackend.controller.backend

import de.rehatech.homeekt.model.attributes
import de.rehatech.smartHomeBackend.Enum.FunctionType
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.Item
import de.rehatech2223.datamodel.Function
import de.rehatech.smartHomeBackend.entities.FunctionValues
import de.rehatech.smartHomeBackend.repositories.FunctionRepository
import de.rehatech.smartHomeBackend.repositories.HomeeRepository
import de.rehatech.smartHomeBackend.services.DeviceService
import de.rehatech.smartHomeBackend.services.FunctionService
import de.rehatech.smartHomeBackend.services.FunctionTypService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
class BackendController @Autowired constructor(

    val openHabController: OpenHabController,
    val deviceService: DeviceService,
    val homeeRepository: HomeeRepository,
    val functionService: FunctionService,
    val functionRepository: FunctionRepository,
    val homeeController: HomeeController,
    val functionTypService: FunctionTypService


) {
    /**
     * @param deviceID
     * @param functionValues
     * @param command
     * @return Boolean
     */
    fun sendCommand(deviceID: String,functionValues: FunctionValues, command:String):Boolean
    {
        if(deviceID.contains("OH:"))
        {

            return openHabController.sendcommand(functionValues.name,command)
        }
        else if(deviceID.contains("HM:"))
        {
            try{
                val id = deviceID.split(":")
                val homeenode = homeeRepository.findById(id[1].toLong()).get()
                homeeController.sendcommand(homeenode.homeeID, functionValues.homeeattrID!!, command.toDouble())

            }
            catch (e:Exception)
            {
                return false
            }
            return true
        }
        else
        {
            return false
        }
    }

    /**
     * @param deviceID
     * @param functionValues
     * @return function?
     */
    fun getFunctionState(deviceID: String,functionValues: FunctionValues):Function?
    {
        if(deviceID.contains("OH:"))
        {

            val item = openHabController.getItemByName(functionValues.name) ?: return null
            return getFunctionFromItem(item, functionValues)
        }
        else if(deviceID.contains("HM:"))
        {
            val nodes = homeeController.getNodes()
            if (nodes != null) {
                for (node in nodes) {
                    if (node.id == functionValues.deviceHomee!!.homeeID )
                    {
                        val atts = node.attributes
                        for (att in atts)
                        {
                            if(att.id == functionValues.homeeattrID)
                            {
                                return getFunctionFromNode(functionValues, att)
                                
                            }
                        }
                    }
                }
            }
        }

        return null

    }

    fun getFunctionFromNode(functionValue: FunctionValues,attribute: attributes):Function?
    {
        return when(functionValue.type) {
            FunctionType.Switch -> {
                var on = false
                if(attribute.state == 1)
                {
                    on = true
                }
                Function(functionName = functionValue.name, functionId = functionValue.id!!, onOff = on, outputValue = attribute.state.toString() )
            }
            FunctionType.Dimmer -> {Function(functionName = functionValue.name, functionId = functionValue.id!!,  outputValue = attribute.state.toString() )}
            else -> {
                null
            }
        }
    }
    fun getFunctionFromItem(item: Item, functionValue: FunctionValues):Function?
    {
        val functionType = functionTypService.functionsTypeOpenHab(item) ?: return null
        when(functionType){
            FunctionType.Switch -> {
                var on = false
                if(item.state == "ON")
                {
                    on = true
                }
                return  Function(functionName = functionValue.name, functionId = functionValue.id!!, onOff = on, outputValue = item.state )
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
            FunctionType.Number -> return  Function(functionName = functionValue.name, functionId = functionValue.id!!, outputValue = item.state )
            FunctionType.Player -> return null
            FunctionType.Rollershutter -> return null
            FunctionType.StringType -> return  Function(functionName = functionValue.name, functionId = functionValue.id!!, outputValue = item.state )
        }

    }

}