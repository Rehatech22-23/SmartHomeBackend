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
import kotlin.collections.ArrayList

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
     * Update OpenHab Device Information
     * @param devices Arraylist Things
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
     * Update Infos from the node
     * @param nodes ArrayList
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
     * Transform a Node to HomeeDevice
     * @param node
     * @return HomeeDevice Object
     */
    fun transformNode(node: nodes):HomeeDevice
    {

        val devicenames = environment.getProperty("homee.device")!!.split('|')
        var nodeName = "Test"
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
     * Transform Thing in Openhab Devices
     * @param things
     * @return OpenHabDevice
     */
    fun transformThing(things: Things):OpenHabDevice
    {
        return  OpenHabDevice(name = things.label, uid = things.UID )

    }

    /**
     *  Get from the DB a list of all Devices
     *  @return List of all Devices
     */
    fun getDeviceList():List<DeviceDTO>
    {
        val listOH = openHabDeviceRepository.findAll().toList()
        val listHM = homeeDeviceRepository.findAll().toList()
        val listDevices = mutableListOf<DeviceDTO>()
        for (oh in listOH)
        {
            val arrayList: ArrayList<Long> = arrayListOf()
            for (devicemethodeID in oh.deviceMethodsIDS)
            {
                arrayList.add(devicemethodeID.id!!)
            }
            listDevices.add(DeviceDTO(deviceName = oh.name, deviceId = oh.getOpenHabID() , functionIds = arrayList))
        }
        for (hm in listHM)
        {
            val arrayList: ArrayList<Long> = arrayListOf()
            for (devicemethodeID in hm.deviceMethodsIDS)
            {
                arrayList.add(devicemethodeID.id!!)
            }
            listDevices.add(DeviceDTO(deviceName = hm.name, deviceId = hm.getHomeeID() , functionIds = arrayList))
        }



        return listDevices.toList()
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

    /**
     *  Get from OpenHab Id a DeviceDTO
     * @param id
     * @return DeviceDTO
     */
    private fun getDeviceOH(id: String): DeviceDTO? {
        var oh: OpenHabDevice? = null
        try{
            val i = id.toLong()
            val ohl = openHabDeviceRepository.findById(i)
            oh = ohl.get()
        }catch(exception: NumberFormatException){

        }

        if(oh == null){
            return null
        }
        val funcIds = ArrayList<Long>()
        for (item in oh.deviceMethodsIDS){
            item.id?.let { funcIds.add(it.toLong()) }
        }

        return DeviceDTO(oh.name, oh.getOpenHabID(), funcIds)
    }


    /**
     *  Get from Homee Id a DeviceDTO
     * @param id
     * @return DeviceDTO
     */
    private fun getDeviceHM(id: String): DeviceDTO? {
        var hm: HomeeDevice? = null
        try{
            id.toLong()
            hm = homeeDeviceRepository.findById(id.toLong()).get()
        }catch(exception: NumberFormatException){

        }

        if(hm == null){
            return null
        }
        val funcIds = ArrayList<Long>()
        for (item in hm.deviceMethodsIDS){
            item.id?.let { funcIds.add(it.toLong()) }
        }

        return DeviceDTO(hm.name, hm.getHomeeID(), funcIds)
    }


}