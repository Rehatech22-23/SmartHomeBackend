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
    private final val url = apiConfiguration.homeeUrl
    private final val user = apiConfiguration.homeeUser
    private final val password = apiConfiguration.homeePassword
    private final val deviceName = apiConfiguration.deviceHomeeName
    val homee: Homee = Homee(url,user, password, device = deviceName)



    /**
     * Update the nodelist
     * @return Boolean
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
     * @param nodeId Int
     * @param attributeId
     * @param value The value to set the node attribut
     * @return success
     */
    fun sendCommand(nodeId:Int, attributeId:Int, value:Float ):Boolean
    {

        val ok = homee.sendNodeBefehl(nodeId, attributeId, value)
        return !(ok == null || ok == false)

    }
}