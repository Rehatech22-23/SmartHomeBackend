package de.rehatech.smartHomeBackend.services

import de.rehatech.homeekt.model.attributes
import de.rehatech.smartHomeBackend.Enum.FunctionType
import de.rehatech.smartHomeBackend.controller.backend.BackendController
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.Item
import de.rehatech.smartHomeBackend.repositories.FunctionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import de.rehatech.smartHomeBackend.entities.FunctionValues
import de.rehatech.smartHomeBackend.repositories.HomeeRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository
import de.rehatech2223.datamodel.FunctionDTO


@Service
class FunctionService  @Autowired constructor(
    val backendController: BackendController,
    val functionRepository: FunctionRepository,
    val openHabRepository: OpenHabRepository,
    val homeeRepository: HomeeRepository,
    val functionTypService: FunctionTypService
) {


    //TODO: Docs
    /**
     * @param functionId
     * @return FunctionDTO
     */
    fun getFunction(functionId: Long): FunctionDTO {

        val funcVal = functionRepository.findById(functionId).get()
        var funcState : FunctionDTO? = null
        if(funcVal.deviceHomee == null) {
          funcState = backendController.getFunctionState(deviceID = funcVal.deviceOpenHab!!.getOpenHabID(), functionValues = funcVal)



        }else if (funcVal.deviceOpenHab == null){
            funcState = backendController.getFunctionState(deviceID = funcVal.deviceHomee!!.getHomeeID(), functionValues = funcVal)        }else{

        }
            if (funcState == null) throw NullPointerException()
        return funcState
    }


    //triggers a function 200 ok (null)=>500 Internal Server Error

    //TODO: Docs
    /**
     * @param deviceId
     * @param functionId
     * @param body
     */
    fun triggerFunc(deviceId: String, functionId: Long, body: Float){
        val funcVal = functionRepository.findById(functionId).get()
        lateinit var command: String
        //einmal für homee und einmal für oh
        when (funcVal.type){ //OH
            FunctionType.Color -> command = "HSB" //nochmal drüberschaun
            FunctionType.Call ->command = ""
            FunctionType.Contact -> command ="OpenClosed"
            FunctionType.Datetime -> command ="DateTime"
            FunctionType.Dimmer -> command ="Percent"
            FunctionType.Group -> command =""
            FunctionType.Image -> command =""
            FunctionType.Location -> command = "Point"
            FunctionType.Number -> command = "Decimal"
            FunctionType.Player -> command = "PlayPause"
            FunctionType.Rollershutter -> command = "Percent" // UpDown?
            FunctionType.StringType -> command = "String"
            FunctionType.Switch -> command = "OnOff"
        }
        backendController.sendCommand(deviceId,funcVal, command)


    }

    //TODO: Docs
    /**
     * @param uid
     * @param item
     */
    fun saveFunctionOpenHab(uid:String, item: Item)
    {
        val openhab = openHabRepository.findOpenHabByUid(uid)
        val functType = functionTypService.functionsTypeOpenHab(item)
        if(functType != null)
        {
            val newFunctionValues = FunctionValues(label = item.label, name = item.name, type = functType , deviceOpenHab = openhab )
            functionRepository.save(newFunctionValues)
        }

    }

    //TODO: Docs
    /**
     * @param attribute
     */
    fun saveFunctionHomee(attribute: attributes)
    {
        val homeeNode = homeeRepository.findHomeeByHomeeID(attribute.node_id)
        val functType = functionTypService.functionsTypeHomee(attribute)
        if(functType != null)
        {
            val newFunctionValues = FunctionValues(label = attribute.name, name = attribute.name, homeeattrID = attribute.id,  type = functType , deviceHomee = homeeNode )
            functionRepository.save(newFunctionValues)
        }
    }






}