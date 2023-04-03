package de.rehatech.smartHomeBackend.controller.backend

import de.rehatech.homeekt.Homee
import de.rehatech.homeekt.model.nodes
import de.rehatech.smartHomeBackend.config.ApiConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

/**
 * A class that handles commands dor HomeeDevices
 *
 * @param apiConfiguration Instance gets automatically autowired into the Controller
 */
@Controller
class HomeeController@Autowired constructor (

    apiConfiguration: ApiConfiguration
) {
    val url = apiConfiguration.homeeUrl
    val user = apiConfiguration.homeeUser
    val password = apiConfiguration.homeePassword
    val deviceName = apiConfiguration.deviceHomeeName
    val homee: Homee = Homee(url,user, password, device = deviceName)



    /**
     * //TODO: Docs
     */
    fun updateNodes():Boolean
    {
        val ok = homee.getallNodes()
        return !(ok == null || ok == false)

    }
    /**
     * The Methode return a List of nodes
     * @return ArrayList<nodes>?
     */
    fun getNodes():ArrayList<nodes>?
    {
        return homee.nodeslist
    }

    /**
     * The Methode send a command to the Homee
     * @param nodeId
     * @param attributeId
     * @param value
     * @return
     */
    fun sendCommand(nodeId:Int, attributeId:Int, value:Float ):Boolean
    {

        val ok = homee.sendNodeBefehl(nodeId, attributeId, value)
        return !(ok == null || ok == false)

    }
}