package de.rehatech.smartHomeBackend.services

import de.rehatech.homeekt.model.attributes
import de.rehatech.smartHomeBackend.Enum.FunctionType
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.Item
import de.rehatech.smartHomeBackend.repositories.FunctionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import de.rehatech.smartHomeBackend.entities.FunctionValues
import de.rehatech.smartHomeBackend.repositories.HomeeRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository


@Service
class FunctionService  @Autowired constructor(
    val functionRepository: FunctionRepository,
    val openHabRepository: OpenHabRepository,
    val homeeRepository: HomeeRepository,
    val functionTypService: FunctionTypService
) {

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