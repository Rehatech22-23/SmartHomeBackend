package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.controller.backend.responsesClass.Things
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

    fun getDevice()
    {

    }
}