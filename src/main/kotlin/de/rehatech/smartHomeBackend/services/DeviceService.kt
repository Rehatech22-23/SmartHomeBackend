package de.rehatech.smartHomeBackend.services

import datamodel.device
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.Things
import de.rehatech.smartHomeBackend.entities.Homee
import de.rehatech.smartHomeBackend.entities.OpenHab
import de.rehatech.smartHomeBackend.repositories.HomeeRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class DeviceService @Autowired constructor(

   val openHabRepository: OpenHabRepository,
   val homeeRepository: HomeeRepository,
   val functionService: FunctionService,
)
{

    fun updateDevicesOpenHab(devices: ArrayList<Things>)
    {
        val listrepo = openHabRepository.findAll().toList()
        if(listrepo.isEmpty())
        {
            for(device in devices)
            {
                trangsformThingAndSave(device)
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
                    trangsformThingAndSave(device)
                }
            }
        }

    }

    private  fun trangsformThingAndSave(things: Things)
    {
        val newDevice = OpenHab(name = things.label, uid = things.UID )
        openHabRepository.save(newDevice)
    }

    /**
     * @return returns a list of all deviceIds as List of DeviceIds (Strings)
     */
    fun getDeviceIdList(): List<String>{
        val result = mutableListOf<String>()
        val listOH = openHabRepository.findAll().toList()
        for (item in listOH){
            result.add(item.getOpenHabID())
        }
        val listHomee = homeeRepository.findAll().toList()
        for (item in listHomee){
            result.add(item.getHomeeID())
        }
        return result
    }

    /**
     * @param deviceId selects which Device gets returned
     * @return returns info over a specific device as Device-object
     */
    fun getDevice(deviceId: String): device?
    {
        val tmp = deviceId.split(":")

        when(tmp.get(0)){
            "OH:" -> return getDeviceOH(tmp.get(1))
            "HM:" -> return getDeviceHM(tmp.get(1))
            else -> throw NullPointerException()
        }

    }

    //Hilfsmethode
    private fun getDeviceOH(id: String): device? {
        var OH: OpenHab? = null
        try{
            id.toLong()
            OH = openHabRepository.findById(id.toLong()).get()
        }catch(Exception: NumberFormatException){

        }

        if(OH == null){
            return null
        }
        val funcIds = ArrayList<Long>()
        for (item in OH.functionValuesIDS){
            item.id?.let { funcIds.add(it.toLong()) }
        }

        return device(OH.name, OH.getOpenHabID(), funcIds)
    }


    //Hilfsmethode
    private fun getDeviceHM(id: String): device? {
        var HM: Homee? = null
        try{
            id.toLong()
            HM = homeeRepository.findById(id.toLong()).get()
        }catch(Exception: NumberFormatException){

        }

        if(HM == null){
            return null
        }
        val funcIds = ArrayList<Long>()
        for (item in HM.functionValuesIDS){
            item.id?.let { funcIds.add(it.toLong()) }
        }

        return device(HM.name, HM.getHomeeID(), funcIds)
    }


    //TODO: implementation of updatedDevices()
    //TODO: edit class diagram
    /**
     * @return returns all ids from updated devices since last update call as List of DeviceIds (Strings)
     */
    fun updatedDevices(): List<String>{
        return emptyList()
    }
}