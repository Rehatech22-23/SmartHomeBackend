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




    fun updateNodes()
    {
        homee.getallNodes()
        Thread.sleep(2000L)

    }
    fun getNodes():ArrayList<nodes>?
    {
        updateNodes()
        return homee.nodeslist
    }

    fun sendcommand(node:Int, attribute:Int, value:Double )
    {
        homee.sendNodeBefehl(node, attribute, value)

    }
}