package de.rehatech.smartHomeBackend.services


import de.rehatech.homeekt.model.nodes
import de.rehatech.smartHomeBackend.response.Things
import de.rehatech.smartHomeBackend.entities.HomeeDevice
import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import de.rehatech.smartHomeBackend.repositories.HomeeDeviceRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabDeviceRepository
import de.rehatech2223.datamodel.DeviceDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service

@Service
class DeviceService @Autowired constructor(

    val openHabDeviceRepository: OpenHabDeviceRepository,
    val homeeDeviceRepository: HomeeDeviceRepository,
    val environment: Environment
)
{

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
                transformThingAndSave(device)
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
                        found = true
                    }
                }
                if(!found)
                {
                    transformThingAndSave(device)
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
                transformNodeAndSave(node)
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
                        found = true
                    }
                }
                if(!found)
                {
                    transformNodeAndSave(node)
                }
            }
        }
    }

    /**
     * //TODO: Docs, refactor trangsformNodeAndSave -> transformNodeAndSave, nodes class -> Nodes class (classes start with Capital Letters)
     * @param node
     */
    private fun transformNodeAndSave(node: nodes)
    {

        val devicenames = environment.getProperty("homee.device")!!.split(',')
        var nodeName = ""
        for (name in devicenames)
        {
            if(name.contains(node.name))
            {
                val splitname = name.split("=")
                nodeName = splitname[1]
            }
        }
        val newDevice = HomeeDevice(name = nodeName, homeeID = node.id)
        homeeDeviceRepository.save(newDevice)
    }


    /**
     * //TODO: Docs, refactor trangsformThingAndSave -> transformThingAndSave
     * @param things
     */
    private  fun transformThingAndSave(things: Things)
    {
        val newDevice = OpenHabDevice(name = things.label, uid = things.UID )
        openHabDeviceRepository.save(newDevice)
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
        return result
    }

    /**
     * @param deviceId selects which Device gets returned
     * @return returns info over a specific device as DeviceDTO-object
     */
    fun getDevice(deviceId: String): DeviceDTO?
    {
        val tmp = deviceId.split(":")

        when(tmp[0]){
            "OH" -> return getDeviceOH(tmp[1])
            "HM" -> return getDeviceHM(tmp[1])
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
        }catch(Exception: NumberFormatException){
            // Todo: Do nothing
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
        }catch(Exception: NumberFormatException){

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


    //TODO: implementation of updatedDevices()
    /**
     * //TODO: Docs
     * @return List<String>
     */
    /**
     * @return returns all ids from updated devices since last update call as List of DeviceIds (Strings)
     */
    fun updatedDevices(): List<String>{
        return emptyList()
    }
}