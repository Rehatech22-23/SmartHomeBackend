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
import java.sql.RowId


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

        val tmp = deviceId.split(":")
        lateinit var command: String;

        when(tmp[0]){
            "OH" -> command = setCommandOH(functionId, body)
            "HM" -> command = setCommandHM(functionId, body)
            else -> throw NullPointerException()
        }

        backendController.sendCommand(deviceId, deviceMethodsRepository.findById(functionId).get(), command)

    }

    private fun openClosed(body: Float): String{
        if(body == 1F){
            return "OPEN"
        }else if (body == 0F){
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
        if(body == 1F){
            return "ON"
        }else if (body == 0F){
            return "OFF"
        }
        return ""       //falscher wert im body
    }


    /**
     * called when Function from a OpenHab Device should get triggered
     */
    private fun setCommandOH(functionId: Long, body: Float): String{
        val funcVal = deviceMethodsRepository.findById(functionId).get()
        lateinit var command: String
        when (funcVal.type) { //OH

            FunctionType.Contact -> command = openClosed(body)
            FunctionType.Dimmer -> command = percent(body)
            FunctionType.Rollershutter -> command = percent(body)
            FunctionType.Switch -> command = onOff(body)
            FunctionType.Call -> command = "REFRESH"
            FunctionType.Image -> command = "REFRESH"


            //REFRESH
            //Fehler: meistens nichts was getriggert wird, sondern was was bei Aufruf refreshed wird
            FunctionType.Color -> command = "HSB"           //momentan für OpenHab-Geräte uninteressant, da wir kein Gerät haben was COLOR benutzt
            FunctionType.Datetime -> command = "DateTime"   //stores nur DATETIME, wird also nicht getriggert
            FunctionType.Group -> command = "-"             //nur zum item nesting, (ohne Command)
            FunctionType.Location -> command = "Point"      //REFRESH, POINT (String of 3 decimals [altitude, longitude, altitude in m], für uns nicht von nutzen)
            FunctionType.Number -> command = "Decimal"      //REFRESH, DECIMAL
            FunctionType.Player -> command = "PlayPause"    //wir benutzen keine Player-Geräte
            FunctionType.StringType -> command = "String"   //stores nur einen String -> REFRESH
        }
        if(command == ""){
            //es wurde ein falscher wert im body übergeben
        }
        return command;
    }

    /**
     * called when Function from a Homee Device should get triggered
     */
    private fun setCommandHM(functionId: Long, body: Float): String{
        lateinit var command: String

        when(functionId){
            26L -> if(checkValueOnOff(body)){
                command = body.toString()
            } //else error
            27L -> if(checkValueDimmingLevel(body)){
                command = body.toString()
            } //else error
            29L -> if(checkValueColor(body)){
                command = body.toString()
            } //else error
            30L -> if(checkValueColorTemperature(body)){
                command = body.toString()
            }//else error
        }
        return command;
    }


    private fun checkValueOnOff(body: Float): Boolean{
        if (body == 0F || body == 1F){ //0: an, 1: aus
            return true
        }
        return false
    }

    private fun checkValueDimmingLevel(body: Float): Boolean{
        if(body >= 0.0 && body <= 100.0){ //Prozent (0: aus, 100: an)
            return true
        }
        return false
    }
    private fun checkValueColor(body: Float): Boolean{
        if(body >= 0F && body <= 16777215){ //HEX Farbcode als Dezimal
            return true
        }
        return false
    }
    private fun checkValueColorTemperature(body: Float): Boolean{
        if (body >= 2000F && body <= 6535){ //Farb Temp als Kelvin
            return true
        }
        return false
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