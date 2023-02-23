package de.rehatech.smartHomeBackend.services

import de.rehatech.homeekt.model.attributes
import de.rehatech.smartHomeBackend.controller.backend.BackendController
import de.rehatech.smartHomeBackend.entities.DeviceMethods
import de.rehatech.smartHomeBackend.enums.FunctionType
import de.rehatech.smartHomeBackend.repositories.DeviceMethodsRepository
import de.rehatech.smartHomeBackend.repositories.HomeeDeviceRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabDeviceRepository
import de.rehatech.smartHomeBackend.response.Item
import de.rehatech2223.datamodel.FunctionDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import de.rehatech.smartHomeBackend.entities.Function


@Service
class FunctionService @Autowired constructor(
    val backendController: BackendController,
    val deviceMethodsRepository: DeviceMethodsRepository,
    val openHabDeviceRepository: OpenHabDeviceRepository,
    val homeeDeviceRepository: HomeeDeviceRepository,
    val functionTypService: FunctionTypService
) {


    //TODO: Docs
    /**
     *
     * @param functionId
     * @return FunctionDTO
     */
    fun getFunction(functionId: Long): FunctionDTO {

        val funcVal = deviceMethodsRepository.findById(functionId).get()
        var funcState: FunctionDTO? = null
        if (funcVal.deviceHomee == null) {
            funcState = backendController.getMethodStatus(
                deviceID = funcVal.deviceOpenHab!!.getOpenHabID(),
                deviceMethods = funcVal
            )


        } else if (funcVal.deviceOpenHab == null) {
            funcState = backendController.getMethodStatus(
                deviceID = funcVal.deviceHomee!!.getHomeeID(),
                deviceMethods = funcVal
            )
        }
        if (funcState == null) throw NullPointerException()
        return funcState
    }


    //triggers a function 200 ok (null)=>500 Internal Server Error

    //TODO: Docs
    /**
     *
     * @param deviceId
     * @param functionId
     * @param body
     */
    fun triggerFunc(deviceId: String, functionId: Long, body: Float) {
        val funcVal = deviceMethodsRepository.findById(functionId).get()
        lateinit var command: String
        //einmal für homee und einmal für oh
        when (funcVal.type) { //OH

            FunctionType.Contact -> command = openClosed(body)
            FunctionType.Dimmer -> command = percent(body)
            FunctionType.Rollershutter -> command = percent(body)
            FunctionType.Switch -> command = onOff(body)




            //Fehler
            FunctionType.Color -> command = "HSB" //nochmal drüberschaun
            FunctionType.Call -> command = ""

            FunctionType.Datetime -> command = "DateTime"

            FunctionType.Group -> command = ""
            FunctionType.Image -> command = ""
            FunctionType.Location -> command = "Point"
            FunctionType.Number -> command = "Decimal"
            FunctionType.Player -> command = "PlayPause"
             // UpDown?
            FunctionType.StringType -> command = "String"

        }
        backendController.sendCommand(deviceId, funcVal, command)


    }

    private fun openClosed(body: Float): String{
        if(body == 0F){
            return "OPEN"
        }else if (body == 1F){
            return "CLOSED"
        }
        return ""       //falscher wert im body
    }

    private fun percent(body: Float): String{
        if(body in 0F..100F){
            return body.toString()
        }
        return ""       //falscher wert im body
    }
    private fun onOff(body: Float): String{
        if(body == 0F){
            return "ON"
        }else if (body == 1F){
            return "OFF"
        }
        return ""       //falscher wert im body
    }



    //TODO: Docs
    /**
     *
     * @param uid
     * @param item
     */
    fun saveFunctionOpenHab(uid: String, item: Item) {
        val openhab = openHabDeviceRepository.findOpenHabByUid(uid)
        val functType = functionTypService.functionsTypeOpenHab(item)
        if (functType != null) {
            val newDeviceMethods =
                DeviceMethods(label = item.label, name = item.name, type = functType, deviceOpenHab = openhab)
            deviceMethodsRepository.save(newDeviceMethods)
        }

    }

    //TODO: Docs, attributes class -> Attributes class (Classes start with Capital Letters)
    /**
     *
     * @param attribute
     */
    fun saveFunctionHomee(attribute: attributes) {
        val homeeNode = homeeDeviceRepository.findHomeeByHomeeID(attribute.node_id)
        val functType = functionTypService.functionsTypeHomee(attribute)
        if (functType != null) {
            val newDeviceMethods = DeviceMethods(
                label = attribute.name,
                name = attribute.name,
                homeeattrID = attribute.id,
                type = functType,
                deviceHomee = homeeNode
            )
            deviceMethodsRepository.save(newDeviceMethods)
        }
    }

    fun equals(functionDTO: FunctionDTO, function: Function): Boolean {
        return true
    }
}