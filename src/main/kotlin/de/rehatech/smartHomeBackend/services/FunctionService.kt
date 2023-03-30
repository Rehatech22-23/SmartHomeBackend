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

/**
 * A Service for the handling of DeviceMethods concerning functions executed by Homee and Openhab devices*
 *
 * @param backendController Instance gets automatically autowired into the Service
 * @param deviceMethodsRepository Instance gets automatically autowired into the Service
 * @param openHabDeviceRepository Instance gets automatically autowired into the Service
 * @param homeeDeviceRepository Instance gets automatically autowired into the Service
 * @param functionTypeService Instance gets automatically autowired into the Service
 */
@Service
class FunctionService @Autowired constructor(
    val backendController: BackendController,
    val deviceMethodsRepository: DeviceMethodsRepository,
    val openHabDeviceRepository: OpenHabDeviceRepository,
    val homeeDeviceRepository: HomeeDeviceRepository,
    val functionTypeService: FunctionTypeService
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
                deviceId = funcVal.deviceOpenHab!!.getOpenHabID(),
                deviceMethods = funcVal
            )


        } else if (funcVal.deviceOpenHab == null) {
            funcState = backendController.getMethodStatus(
                deviceId = funcVal.deviceHomee!!.getHomeeID(),
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
        val tmp = deviceId.split(":")
        val command: String = when (tmp[0]) {
            "OH" -> setCommandOH(functionId, body)
            "HM" -> setCommandHM(functionId, body)
            else -> {""}
        }

        if(command == ""){
            throw IllegalArgumentException() //falscher wert im body übergeben
        }else if(command == "-1"){
            throw NoSuchMethodError() //falsches Homeeattr übergeben (eine nummer die keinen sinn ergibt)
        }
        backendController.sendCommand(deviceId, deviceMethodsRepository.findById(functionId).get(), command)
    }

    private fun openClosed(body: Float): String {
        if (body == 1F) {
            return "OPEN"
        } else if (body == 0F) {
            return "CLOSED"
        }
        return ""       //falscher wert im body
    }

    private fun percent(body: Float): String {
        if (body in 0F..100F) {
            return body.toString()
        }
        return ""       //falscher wert im body
    }

    private fun onOff(body: Float): String {
        if (body == 1F) {
            return "ON"
        } else if (body == 0F) {
            return "OFF"
        }
        return ""       //falscher wert im body
    }

    private fun playPause(body: Float): String{
        val command = when (body){
            0F -> "PLAY"
            1F -> "NEXT"
            2F -> "PREVIOUS"
            3F -> "PAUSE"
            4F -> "REWIND"
            5F -> "FASTFORWARD"
            6F -> "REFRESH"
            else -> ""
        }
        /*if(body == 1F){
            return "PLAY"
        }else if(body == 0F){
            return "PAUSE"
        }*/
        return command
    }

    private fun airPurifierState(body: Float): String{
        val command: String = when(body){
            0F -> "s"
            1F -> "1"
            2F -> "2"
            3F -> "3"
            4F -> "t"
            else -> ""
        }
        return command
    }


    /**
     * called when Function from a OpenHab Device should get triggered
     */
    private fun setCommandOH(functionId: Long, body: Float): String {
        val funcVal = deviceMethodsRepository.findById(functionId).get()
        val command: String = when (funcVal.type) { //OH
            FunctionType.Contact -> openClosed(body)
            FunctionType.Dimmer -> percent(body)
            FunctionType.Rollershutter -> percent(body)
            FunctionType.Switch -> onOff(body)
            FunctionType.Call -> "REFRESH"
            FunctionType.Image -> "REFRESH"
            FunctionType.Player -> playPause(body)
            FunctionType.Air -> airPurifierState(body)

            //REFRESH
            //Fehler: meistens nichts was getriggert wird, sondern was was bei Aufruf refreshed wird
            FunctionType.Color -> "HSB"           //momentan für OpenHab-Geräte uninteressant, da wir kein Gerät haben was COLOR benutzt
            FunctionType.Datetime -> "DateTime"   //stores nur DATETIME, wird also nicht getriggert
            FunctionType.Group -> "-"             //nur zum item nesting, (ohne Command)
            FunctionType.Location -> "Point"      //REFRESH, POINT (String of 3 decimals [altitude, longitude, altitude in m], für uns nicht von nutzen)
            FunctionType.Number -> "Decimal"      //REFRESH, DECIMAL
            FunctionType.StringType -> "String"   //stores nur einen String -> REFRESH
        }
        if (command == "") {
            //es wurde ein falscher wert im body übergeben
        }
        return command
    }

    /**
     * called when Function from a Homee Device should get triggered
     */
    private fun setCommandHM(functionId: Long, body: Float): String {
        val funcVal = deviceMethodsRepository.findById(functionId).get()
        val command: String = when (funcVal.type) {
            FunctionType.Switch -> checkValueOnOff(body) //else error
            FunctionType.Dimmer -> checkValueDimmingLevel(body) //else error
            FunctionType.Color -> checkValueColor(body)//else error
            else -> {"-1"}
        }
        if(command == ""){
            //fehlerbehandlung (falscher wert im body übergeben, wert passt nicht zum homeeattr)
        }else if(command == "-1"){
            //fehlerbehandlung (falsches homeeattr übergeben)
        }
        return command
    }


    private fun checkValueOnOff(body: Float): String {
        if (body == 0F || body == 1F) { //0: an, 1: aus
            return body.toString()
        }
        return ""
    }

    private fun checkValueDimmingLevel(body: Float): String {
        if (body in 0.0..100.0) { //Prozent (0: aus, 100: an)
            return body.toString()
        }
        return ""
    }

    private fun checkValueColor(body: Float): String {
        if (body in 0F..16777215F) { //HEX Farbcode als Dezimal
            return body.toString()
        }
        return ""
    }


    //TODO: Docs
    /**
     *
     * @param uid
     * @param item
     */
    fun saveFunctionOpenHab(uid: String, item: Item) {
        val openhab = openHabDeviceRepository.findOpenHabByUid(uid)
        val functType = functionTypeService.functionsTypeOpenHab(item)
        if (functType != null) {
            val newDeviceMethods =
                DeviceMethods(label = item.label, name = item.name, type = functType, deviceOpenHab = openhab)
            var found = false
            val allDevice = deviceMethodsRepository.findAll()
            for (device in allDevice)
            {
                if(device.deviceOpenHab != null)
                {
                    if( device.deviceOpenHab == newDeviceMethods.deviceOpenHab)
                    {
                        if( device.label == newDeviceMethods.label)
                        {
                            if( device.name == newDeviceMethods.name)
                            {
                                found = true
                            }
                        }
                    }
                }
            }
            if (!found) {
                deviceMethodsRepository.save( newDeviceMethods)
            }
        }

    }

    //TODO: Docs, attributes class -> Attributes class (Classes start with Capital Letters)
    /**
     *
     * @param attribute
     */
    fun saveFunctionHomee(attribute: attributes) {
        val allnodes = deviceMethodsRepository.findAll()
        var found = false
        val newDeviceMethods = transformAttribut(attribute)
        if(newDeviceMethods !=null) {
            for (node in allnodes) {
                if (node.deviceHomee != null) {

                    if (newDeviceMethods.deviceHomee == node.deviceHomee) {
                        if (newDeviceMethods.label == node.label) {
                            if (newDeviceMethods.name == node.name) {
                                if (newDeviceMethods.homeeattrID == node.homeeattrID) {
                                    found = true
                                }
                            }
                        }
                    }
                }
            }
            if (!found) {
                deviceMethodsRepository.save(newDeviceMethods)
            }
        }

    }

    fun transformAttribut(attribute: attributes): DeviceMethods?
    {
        val homeeNode = homeeDeviceRepository.findHomeeByHomeeID(attribute.node_id)
        val functType = functionTypeService.functionsTypeHomee(attribute)
        if (functType != null) {

            val label = when (functType) {
                FunctionType.Switch -> "OnOff"
                FunctionType.Dimmer -> "Dimmer"
                FunctionType.Color -> "Color"
                else -> "error"
            }
             return DeviceMethods(
                label = label,
                name = attribute.name,
                homeeattrID = attribute.id,
                type = functType,
                deviceHomee = homeeNode
            )
        }
        return null
    }


}