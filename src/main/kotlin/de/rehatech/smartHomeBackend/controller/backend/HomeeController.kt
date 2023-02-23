package de.rehatech.smartHomeBackend.controller.backend

import de.rehatech.homeekt.Homee
import de.rehatech.homeekt.model.nodes
import de.rehatech.smartHomeBackend.config.ApiConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller

@Controller
class HomeeController@Autowired constructor (

    apiConfiguration: ApiConfiguration
) {
    val url = apiConfiguration.homeeUrl
    val user = apiConfiguration.homeeUser
    val password = apiConfiguration.homeePassword
    val homee: Homee = Homee(url,user, password)



    /**
     * //TODO: Docs
     */
    fun updateNodes()
    {
        homee.getallNodes()
        Thread.sleep(2000L)

    }
    /**
     * //TODO: Docs
     * @return ArrayList<nodes>?
     */
    fun getNodes():ArrayList<nodes>?
    {
        updateNodes()
        return homee.nodeslist
    }

    /**
     * //TODO: Docs, refactor sendcommand -> sendCommand
     * @param node
     * @param attribute
     * @param value
     */
    fun sendcommand(node:Int, attribute:Int, value:Double )
    {
        homee.sendNodeBefehl(node, attribute, value)

    }
}