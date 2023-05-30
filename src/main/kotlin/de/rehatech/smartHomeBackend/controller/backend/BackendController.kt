package de.rehatech.smartHomeBackend.controller.backend

import de.rehatech.homeekt.model.attributes
import de.rehatech.smartHomeBackend.enums.FunctionType
import de.rehatech.smartHomeBackend.response.Item
import de.rehatech.smartHomeBackend.entities.DeviceMethods
import de.rehatech.smartHomeBackend.repositories.HomeeDeviceRepository
import de.rehatech.smartHomeBackend.services.FunctionTypeService
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.datamodel.util.RangeDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

/**
 * A class determining if a DeviceMethods belongs to a Homee device or OpenHab device and routes
 * these commands either to the OpenHabController or HomeeController. It works essentially as a
 * pre determination Controller above the actual Controller layer. That is necessary as OpenHab
 * and Homee use different APIs
 *
 * @param openHabController Instance gets automatically autowired into the Controller
 * @param homeeDeviceRepository Instance gets automatically autowired into the Controller
 * @param homeeController Instance gets automatically autowired into the Controller
 * @param functionTypeService Instance gets automatically autowired into the Controller
 */
@Controller
class BackendController @Autowired constructor(

    val openHabController: OpenHabController,
    val homeeDeviceRepository: HomeeDeviceRepository,
    val homeeController: HomeeController,
    val functionTypeService: FunctionTypeService


) {
    /**
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
     * Get a Status from a Method
     * @param deviceId Homee or OpenHab Id
     * @param deviceMethods Method from a device
     * @param reload Send a Refresh Command OpenHab
     * @return FunctionDTO? Get the actuall status in form from FunctionDTO
     */
    fun getMethodStatus(deviceId: String, deviceMethods: DeviceMethods, reload: Boolean =true): FunctionDTO? { //Für Homee Und Openhab Methoden
        if (deviceId.contains("OH:")) {
            if (reload && (deviceMethods.type != FunctionType.Datetime||deviceMethods.type != FunctionType.Group) )
            {
                openHabController.sendCommand(deviceMethods.name, "REFRESH")
                if (deviceMethods.type == FunctionType.StringType){
                    Thread.sleep(6200L)
                }
                else
                {
                    Thread.sleep(1000L)
                }

            }
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
     * Transform a Node attribute in FunctionDTO
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
                    functionName = deviceMethod.label,
                    functionId = deviceMethod.id!!,
                    onOff = on,
                ).build()
            }

            FunctionType.Dimmer -> {
                FunctionDTO.Builder(
                    functionName = deviceMethod.label,
                    functionId = deviceMethod.id!!,
                    rangeDTO = RangeDTO(
                        attribute.minimum.toDouble(), attribute.maximum.toDouble(), attribute.state.toDouble()
                    )
                ).build()
            }

            FunctionType.Color -> {
                FunctionDTO.Builder(
                    functionName = deviceMethod.label,
                    functionId = deviceMethod.id!!,
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
     * transform a Item to FunctionDTO
     * @param item OpenHab Item
     * @param deviceMethod
     * @return FunctionDTO?
     */
    fun getFunctionFromItem(item: Item, deviceMethod: DeviceMethods): FunctionDTO? {
        val functionType = functionTypeService.functionsTypeOpenHab(item) ?: return null
        when (functionType) {
            FunctionType.Switch -> {
                var on = false
                if (item.state == "ON") {
                    on = true
                }
                return FunctionDTO.Builder(
                    functionName = deviceMethod.label,
                    functionId = deviceMethod.id!!,
                    onOff = on,
                ).build()
            }
            // ToDo return null durch die richtige umwandkung ersetzen
            FunctionType.Color -> return null
            FunctionType.Call -> return null
            FunctionType.Contact -> return FunctionDTO.Builder(
                functionName = deviceMethod.label,
                functionId = deviceMethod.id!!,
                outputValue = item.state
            ).build()

            FunctionType.Datetime -> return null
            FunctionType.Dimmer -> {
                try {
                    if ("makeString" in item.tags)
                    {
                        return FunctionDTO.Builder(
                            functionName = deviceMethod.label,
                            functionId = deviceMethod.id!!,
                            outputValue = item.state,
                        ).build()
                    }
                    return FunctionDTO.Builder(
                        functionName = deviceMethod.label,
                        functionId = deviceMethod.id!!,
                        rangeDTO = RangeDTO(
                            item.stateDescription.minimum,
                            item.stateDescription.maximum,
                            item.state.toDouble()
                        )
                    ).build()
                }
                catch (ex: NumberFormatException)
                {
                    return FunctionDTO.Builder(
                        functionName = deviceMethod.label,
                        functionId = deviceMethod.id!!,
                        outputValue = item.state,
                    ).build()
                }
            }

            FunctionType.Group -> return null
            FunctionType.Image -> return null
            FunctionType.Location -> return null
            FunctionType.Air -> return FunctionDTO.Builder(
                functionName = deviceMethod.label,
                functionId = deviceMethod.id!!,
                outputValue = item.state
            ).build()

            FunctionType.Number -> {
                try {
                    if ("makeString" in item.tags)
                    {
                        return FunctionDTO.Builder(
                            functionName = deviceMethod.label,
                            functionId = deviceMethod.id!!,
                            outputValue = item.state,
                        ).build()
                    }
                    val itemsplit = item.state.split(" ")
                    return FunctionDTO.Builder(
                        functionName = deviceMethod.label,
                        functionId = deviceMethod.id!!,
                        rangeDTO = RangeDTO(
                            item.stateDescription.minimum,
                            item.stateDescription.maximum,
                            itemsplit[0].toDouble()
                        )
                    ).build()
                }
                catch (ex: NumberFormatException)
                {
                    return FunctionDTO.Builder(
                        functionName = deviceMethod.label,
                        functionId = deviceMethod.id!!,
                        outputValue = item.state,
                    ).build()
                }
                catch (ex: NullPointerException)
                {
                    return FunctionDTO.Builder(
                        functionName = deviceMethod.label,
                        functionId = deviceMethod.id!!,
                        outputValue = item.state,
                    ).build()
                }
            }

            FunctionType.Player -> return FunctionDTO.Builder(
                functionName = deviceMethod.label,
                functionId = deviceMethod.id!!,
                isPlayer = true,
                outputValue = item.state
            ).build()

            FunctionType.Rollershutter -> {
                try {
                    if ("makeString" in item.tags)
                    {
                        return FunctionDTO.Builder(
                            functionName = deviceMethod.label,
                            functionId = deviceMethod.id!!,
                            outputValue = item.state,
                        ).build()
                    }
                    return FunctionDTO.Builder(
                        functionName = deviceMethod.label,
                        functionId = deviceMethod.id!!,
                        rangeDTO = RangeDTO(
                            item.stateDescription.minimum,
                            item.stateDescription.maximum,
                            item.state.toDouble()
                        )
                    ).build()
                }
                catch (ex: NumberFormatException)
                {
                    return FunctionDTO.Builder(
                        functionName = deviceMethod.label,
                        functionId = deviceMethod.id!!,
                        outputValue = item.state,
                    ).build()
                }
            }

            FunctionType.StringType -> return FunctionDTO.Builder(
                functionName = deviceMethod.label,
                functionId = deviceMethod.id!!,
                outputValue = item.state
            ).build()

        }

    }

}