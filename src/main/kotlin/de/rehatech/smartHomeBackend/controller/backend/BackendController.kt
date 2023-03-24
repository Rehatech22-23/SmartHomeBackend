package de.rehatech.smartHomeBackend.controller.backend

import de.rehatech.homeekt.model.attributes
import de.rehatech.smartHomeBackend.enums.FunctionType
import de.rehatech.smartHomeBackend.response.Item
import de.rehatech.smartHomeBackend.entities.DeviceMethods
import de.rehatech.smartHomeBackend.repositories.HomeeDeviceRepository
import de.rehatech.smartHomeBackend.services.FunctionTypService
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.datamodel.util.RangeDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
class BackendController @Autowired constructor(

    val openHabController: OpenHabController,
    val homeeDeviceRepository: HomeeDeviceRepository,
    val homeeController: HomeeController,
    val functionTypService: FunctionTypService


) {
    /** //TODO: deviceID-> deviceId
     * decides from deviceID if its a homee or a OpenHab Device and calls the sendCommand-Function from the
     * corresponding Controller Class (either HomeeController or OpenHabController)
     * @param deviceId String starting either with "OH:" or "HM:" to indicate if its a OpenHab or a Homee Device, after the ":" follows the Id (Long)
     * @param deviceMethods
     * @param command
     * @return Boolean
     */
    fun sendCommand(deviceId: String, deviceMethods: DeviceMethods, command:String):Boolean{
        if(deviceId.contains("OH:")){
            return openHabController.sendCommand(deviceMethods.name,command)
        }
        else if(deviceId.contains("HM:")){
            try{
                val id = deviceId.split(":")
                val homeenode = homeeDeviceRepository.findById(id[1].toLong()).get()
                homeeController.sendCommand(homeenode.homeeID, deviceMethods.homeeattrID!!, command.toFloat())
            }
            catch (e:Exception){
                return false
            }
            return true
        }
        else{
            return false
        }
    }

    /**
     * @param deviceId
     * @param deviceMethods
     * @return FunctionDTO?
     */
    fun getMethodStatus(deviceId: String, deviceMethods: DeviceMethods): FunctionDTO? { //Für Homee Und Openhab Methoden
        if (deviceId.contains("OH:")) {

            val item = openHabController.getItemByName(deviceMethods.name) ?: return null
            return getFunctionFromItem(item, deviceMethods)
        } else if (deviceId.contains("HM:")) {
            val nodes = homeeController.getNodes()
            if (nodes != null) {
                for (node in nodes) {
                    if (node.id == deviceMethods.deviceHomee!!.homeeID) {
                        val atts = node.attributes
                        for (att in atts) {
                            if (att.id == deviceMethods.homeeattrID) {
                                return getFunctionFromNode(deviceMethods, att)

                            }
                        }
                    }
                }
            }
        }

        return null

    }

    /**
     * @param deviceMethod
     * @param attribute
     * @return FunctionDTO?
     */
    fun getFunctionFromNode(deviceMethod: DeviceMethods, attribute: attributes): FunctionDTO? {
        return when (deviceMethod.type) {
            FunctionType.Switch -> {
                var on = false
                if (attribute.state == 1) {
                    on = true
                }
                FunctionDTO.Builder(
                    functionName = deviceMethod.name,
                    functionId = deviceMethod.id!!,
                    onOff = on,
                    outputValue = attribute.state.toString()
                ).build()
            }

            FunctionType.Dimmer -> {
                FunctionDTO.Builder(
                    functionName = deviceMethod.name,
                    functionId = deviceMethod.id!!,
                    outputValue = attribute.state.toString(),
                    rangeDTO = RangeDTO(
                        attribute.minimum.toDouble(), attribute.maximum.toDouble(), attribute.state.toDouble()
                    )
                ).build()
            }

            else -> {
                null
            }
        }
    }

    /**
     * //TODO: Docs
     * @param item
     * @param deviceMethod
     * @return FunctionDTO?
     */
    fun getFunctionFromItem(item: Item, deviceMethod: DeviceMethods): FunctionDTO? {
        val functionType = functionTypService.functionsTypeOpenHab(item) ?: return null
        when (functionType) {
            FunctionType.Switch -> {
                var on = false
                if (item.state == "ON") {
                    on = true
                }
                return FunctionDTO.Builder(
                    functionName = deviceMethod.name,
                    functionId = deviceMethod.id!!,
                    onOff = on,
                    outputValue = item.state
                ).build()
            }
            // ToDo return null durch die richtige umwandkung ersetzen
            FunctionType.Color -> return null
            FunctionType.Call -> return null
            FunctionType.Contact -> return FunctionDTO.Builder(
                functionName = deviceMethod.name,
                functionId = deviceMethod.id!!,
                outputValue = item.state
            ).build()

            FunctionType.Datetime -> return null
            FunctionType.Dimmer -> return FunctionDTO.Builder(
                functionName = deviceMethod.name,
                functionId = deviceMethod.id!!,
                outputValue = item.state,
                rangeDTO = RangeDTO(item.stateDescription.minimum, item.stateDescription.maximum, item.state.toDouble())
            ).build()

            FunctionType.Group -> return null
            FunctionType.Image -> return null
            FunctionType.Location -> return null
            FunctionType.Number -> return FunctionDTO.Builder(
                functionName = deviceMethod.name,
                functionId = deviceMethod.id!!,
                outputValue = item.state
            ).build()

            FunctionType.Player -> return null
            FunctionType.Rollershutter -> return FunctionDTO.Builder(
                functionName = deviceMethod.name,
                functionId = deviceMethod.id!!,
                outputValue = item.state,
                rangeDTO = RangeDTO(item.stateDescription.minimum, item.stateDescription.maximum, item.state.toDouble())
            ).build()

            FunctionType.StringType -> return FunctionDTO.Builder(
                functionName = deviceMethod.name,
                functionId = deviceMethod.id!!,
                outputValue = item.state
            ).build()
        }

    }

}