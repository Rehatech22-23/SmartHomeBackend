package de.rehatech.smartHomeBackend.controller.backend


import datamodel.function
import de.rehatech.smartHomeBackend.entities.FunctionValues
import de.rehatech.smartHomeBackend.repositories.FunctionRepository
import de.rehatech.smartHomeBackend.services.DeviceService
import de.rehatech.smartHomeBackend.services.FunctionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Controller

@Controller
class BackendController @Autowired constructor(

    val openHabController: OpenHabController,
    val deviceService: DeviceService,
    val functionService: FunctionService,
    val functionRepository: FunctionRepository


) {


    @Scheduled(cron="0 1 1 * * *")
    fun updateDevices()
    {
        val allOpenHabDevice = openHabController.getDevices()
        if(allOpenHabDevice != null)
        {
            deviceService.updateDevicesOpenHab(allOpenHabDevice)
            for (device in allOpenHabDevice)
            {
                val channels= device.channels
                for(channel in channels)
                {
                    for(itemname in channel.linkedItems)
                    {
                        val item = openHabController.getItemByName(itemname)
                        if (item != null)
                        {
                            functionService.saveFunctionOpenHab(device.UID,item)
                        }
                    }
                }
            }
        }


    }

    fun sendCommand(deviceID: String,functionValues: FunctionValues, command:String):Boolean
    {
        if(deviceID.contains("OH:"))
        {

            return openHabController.sendcommand(functionValues.name,command)
        }
        else if(deviceID.contains("HM:"))
        {
            return false
        }
        else
        {
            return false
        }
    }


    fun getFunctionState(deviceID: String,functionValues: FunctionValues):function?
    {
        if(deviceID.contains("OH:"))
        {

            val item = openHabController.getItemByName(functionValues.name) ?: return null
            return functionService.getFunctionFromItem(item, functionValues)
        }
        else if(deviceID.contains("HM:"))
        {
            return null
        }
        else
        {
            return null
        }
    }




}