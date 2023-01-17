package de.rehatech.smartHomeBackend.services

import datamodel.device
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.Things
import de.rehatech.smartHomeBackend.controller.backend.responsesClass.hilfsclass.Device
import de.rehatech.smartHomeBackend.entities.OpenHab
import de.rehatech.smartHomeBackend.repositories.HomeeRepository
import de.rehatech.smartHomeBackend.repositories.OpenHabRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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

    //TODO: implementation of getDeviceIdList()
    //TODO: adjust class diagram (getDevices() -> getDeviceIDList())
    /**
     * @return returns a list of deviceIds as List of DeviceIds (Strings)
     */
    fun getDeviceIdList(): List<String>{
        return emptyList()
    }

    //TODO: implementation of getDevice(deviceId: String) + Device not nullable
    //TODO:  edit class diagram
    /**
     * @param deviceId selects which Device gets returned
     * @return returns info over a specific device as Device-object
     */
    fun getDevice(deviceId: String): Device?
    {
        var devi = Device("nameh","aidi", intArrayOf(1,2,3,4,5))

        return devi
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