package de.rehatech.smartHomeBackend.services


import de.rehatech.homeekt.model.nodes
import de.rehatech.smartHomeBackend.response.Things
import de.rehatech.smartHomeBackend.entities.HomeeDevice
import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import de.rehatech.smartHomeBackend.repositories.HomeeDeviceRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabDeviceRepository
import de.rehatech2223.datamodel.DeviceDTO
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

/**
 * A Service class handling the business logic of devices
 *
 * @param openHabDeviceRepository Instance gets automatically autowired into the Service
 * @param homeeDeviceRepository Instance gets automatically autowired into the Service
 * @param environment Instance gets automatically autowired into the Service
 */
@Service
class DeviceService @Autowired constructor(

    val openHabDeviceRepository: OpenHabDeviceRepository,
    val homeeDeviceRepository: HomeeDeviceRepository,
    val environment: Environment
)
{
    private val log: Logger = LoggerFactory.getLogger(DeviceService::class.java)

    /**
     * //TODO: Docs
     * @param devices
     */
    fun updateDevicesOpenHab(devices: ArrayList<Things>)
    {
        val listrepo = openHabDeviceRepository.findAll().toList()
        if(listrepo.isEmpty())
        {
            for(device in devices)
            {
                openHabDeviceRepository.save(transformThing(device))
            }
        }
        else
        {
            val devicesIt = devices.iterator()
            while(devicesIt.hasNext())
            {
                val device = devicesIt.next()
                var found = false
                for( data in listrepo)
                {
                    if(data.uid == device.UID)
                    {
                        if (data.name == device.label)
                        {
                            found = true
                        }
                        else
                        {
                            found = true
                            data.name = device.label
                            openHabDeviceRepository.save(data)
                        }

                    }
                }
                if(!found)
                {
                    openHabDeviceRepository.save(transformThing(device))
                }
            }
        }
    }




    /**
     * //TODO: Docs, refactor: nodes class -> Nodes class (classes start with Capital Letters)
     * @param nodes
     */
    fun updateNodeHomee(nodes:ArrayList<nodes>)
    {
        val listnodes = homeeDeviceRepository.findAll().toList()
        if(listnodes.isEmpty())
        {
            for(node in nodes)
            {
                homeeDeviceRepository.save(transformNode(node))
            }
        }
        else
        {
            val nodesIt = nodes.iterator()
            while(nodesIt.hasNext())
            {
                val node = nodesIt.next()
                var found = false
                for( data in listnodes)
                {
                    if(data.homeeID == node.id)
                    {
                        val newnode = transformNode(node)
                        if (data.name == newnode.name )
                        {
                            found = true
                        }
                        else
                        {
                            data.name = newnode.name
                            homeeDeviceRepository.save(data)
                            found = true
                        }

                    }
                }
                if(!found)
                {
                    homeeDeviceRepository.save(transformNode(node))
                }
            }
        }
    }

    /**
     * //TODO: Docs, refactor trangsformNodeAndSave -> transformNodeAndSave, nodes class -> Nodes class (classes start with Capital Letters)
     * @param node
     */
    fun transformNode(node: nodes):HomeeDevice
    {

        val devicenames = environment.getProperty("homee.device")!!.split('|')
        var nodeName = ""
        for (name in devicenames)
        {
            if(name.contains(node.name))
            {
                val splitname = name.split("=")
                nodeName = splitname[1]
            }
        }
        return HomeeDevice(name = nodeName, homeeID = node.id)

    }


    /**
     * @param things
     */
    fun transformThing(things: Things):OpenHabDevice
    {
        return  OpenHabDevice(name = things.label, uid = things.UID )

    }


    /**
     * @return returns a list of all deviceIds as List of DeviceIds (Strings)
     */
    fun getDeviceIdList(): List<String>{
        val result = mutableListOf<String>()
        val listOH = openHabDeviceRepository.findAll().toList()
        for (item in listOH){
            result.add(item.getOpenHabID())
        }
        val listHomee = homeeDeviceRepository.findAll().toList()
        for (item in listHomee){
            result.add(item.getHomeeID())
        }
        log.info("All Devices are retrieved from DB")
        return result
    }

    /**
     * @param deviceId selects which Device gets returned
     * @return returns info over a specific device as DeviceDTO-object
     */
    fun getDevice(deviceId: String): DeviceDTO?
    {
        val tmp = deviceId.split(":")

        return when(tmp[0]){
            "OH" -> getDeviceOH(tmp[1])
            "HM" -> getDeviceHM(tmp[1])
            else -> throw NullPointerException()
        }

    }

    //Hilfsmethode
    private fun getDeviceOH(id: String): DeviceDTO? {
        var OH: OpenHabDevice? = null
        try{
            val i = id.toLong()
            val OHL = openHabDeviceRepository.findById(i)
            OH = OHL.get()
        }catch(exception: NumberFormatException){

        }

        if(OH == null){
            return null
        }
        val funcIds = ArrayList<Long>()
        for (item in OH.deviceMethodsIDS){
            item.id?.let { funcIds.add(it.toLong()) }
        }

        return DeviceDTO(OH.name, OH.getOpenHabID(), funcIds)
    }


    //Hilfsmethode
    private fun getDeviceHM(id: String): DeviceDTO? {
        var HM: HomeeDevice? = null
        try{
            id.toLong()
            HM = homeeDeviceRepository.findById(id.toLong()).get()
        }catch(exception: NumberFormatException){

        }

        if(HM == null){
            return null
        }
        val funcIds = ArrayList<Long>()
        for (item in HM.deviceMethodsIDS){
            item.id?.let { funcIds.add(it.toLong()) }
        }

        return DeviceDTO(HM.name, HM.getHomeeID(), funcIds)
    }


}