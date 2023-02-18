package de.rehatech.smartHomeBackend.controller.backend

import de.rehatech.homeekt.model.attributes
import de.rehatech.smartHomeBackend.Enum.FunctionType
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.Item
import de.rehatech.smartHomeBackend.entities.FunctionValues
import de.rehatech.smartHomeBackend.repositories.FunctionRepository
import de.rehatech.smartHomeBackend.repositories.HomeeRepository
import de.rehatech.smartHomeBackend.services.DeviceService
import de.rehatech.smartHomeBackend.services.FunctionService
import de.rehatech.smartHomeBackend.services.FunctionTypService
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.datamodel.util.RangeDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
class BackendController @Autowired constructor(

    val openHabController: OpenHabController,
    val homeeRepository: HomeeRepository,
    val homeeController: HomeeController,
    val functionTypService: FunctionTypService


) {
    /**
     * //TODO: Docs, refactor deviceID -> deviceId
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
     * //TODO: Docs, refactor deviceID -> deviceId
     * @param deviceID
     * @param functionValues
     * @return FunctionDTO?
     */
    fun getFunctionState(deviceID: String,functionValues: FunctionValues): FunctionDTO?
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

    /**
     * //TODO: Docs
     * @param functionValue
     * @param attribute
     * @return FunctionDTO?
     */
    fun getFunctionFromNode(functionValue: FunctionValues,attribute: attributes):FunctionDTO?
    {
        return when(functionValue.type) {
            FunctionType.Switch -> {
                var on = false
                if(attribute.state == 1)
                {
                    on = true
                }
                FunctionDTO(functionName = functionValue.name, functionId = functionValue.id!!, onOff = on, outputValue = attribute.state.toString() )
            }
            FunctionType.Dimmer -> {FunctionDTO(functionName = functionValue.name, functionId = functionValue.id!!,  outputValue = attribute.state.toString(), rangeDTO = RangeDTO(
                attribute.minimum.toDouble(), attribute.maximum.toDouble(), attribute.state.toDouble()
            ) )}
            else -> {
                null
            }
        }
    }

    /**
     * //TODO: Docs
     * @param item
     * @param functionValue
     * @return FunctionDTO?
     */
    fun getFunctionFromItem(item: Item, functionValue: FunctionValues):FunctionDTO?
    {
        val functionType = functionTypService.functionsTypeOpenHab(item) ?: return null
        when(functionType){
            FunctionType.Switch -> {
                var on = false
                if(item.state == "ON")
                {
                    on = true
                }
                return  FunctionDTO(functionName = functionValue.name, functionId = functionValue.id!!, onOff = on, outputValue = item.state )
            }
            // ToDo return null durch die richtige umwandkung ersetzen
            FunctionType.Color -> return null
            FunctionType.Call -> return null
            FunctionType.Contact -> return FunctionDTO(functionName = functionValue.name, functionId = functionValue.id!!, outputValue = item.state )
            FunctionType.Datetime -> return null
            FunctionType.Dimmer -> return FunctionDTO(functionName = functionValue.name, functionId = functionValue.id!!, outputValue = item.state, rangeDTO = RangeDTO(item.stateDescription.minimum, item.stateDescription.maximum, item.state.toDouble()) )
            FunctionType.Group -> return null
            FunctionType.Image -> return null
            FunctionType.Location -> return null
            FunctionType.Number -> return  FunctionDTO(functionName = functionValue.name, functionId = functionValue.id!!, outputValue = item.state )
            FunctionType.Player -> return null
            FunctionType.Rollershutter -> return FunctionDTO(functionName = functionValue.name, functionId = functionValue.id!!, outputValue = item.state, rangeDTO = RangeDTO(item.stateDescription.minimum, item.stateDescription.maximum, item.state.toDouble()) )
            FunctionType.StringType -> return  FunctionDTO(functionName = functionValue.name, functionId = functionValue.id!!, outputValue = item.state )
        }

    }

}