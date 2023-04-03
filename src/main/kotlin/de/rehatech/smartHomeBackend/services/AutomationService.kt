package de.rehatech.smartHomeBackend.services

import de.rehatech.homeekt.model.nodes
import de.rehatech.smartHomeBackend.controller.backend.BackendController
import de.rehatech.smartHomeBackend.controller.backend.HomeeController
import de.rehatech.smartHomeBackend.controller.backend.OpenHabController
import de.rehatech.smartHomeBackend.entities.DeviceMethods
import de.rehatech.smartHomeBackend.entities.HomeeDevice
import de.rehatech.smartHomeBackend.entities.OpenHabDevice
import de.rehatech.smartHomeBackend.entities.TriggerEventByDevice
import de.rehatech.smartHomeBackend.enums.FunctionType
import de.rehatech.smartHomeBackend.repositories.*
import de.rehatech2223.datamodel.FunctionDTO
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalTime
import java.util.*

/**
 * A Service class handling the automated execution of Routines
 *
 * @param routineRepository Instance gets automatically autowired into the Service
 * @param functionService Instance gets automatically autowired into the Service
 * @param backendController Instance gets automatically autowired into the Service
 * @param triggerTimeRepository Instance gets automatically autowired into the Service
 * @param deviceMethodsRepository Instance gets automatically autowired into the Service
 * @param openHabController Instance gets automatically autowired into the Service
 * @param homeeController Instance gets automatically autowired into the Service
 * @param deviceService Instance gets automatically autowired into the Service
 */
@Service
class AutomationService  @Autowired constructor(

    val routineRepository: RoutineRepository,
    val triggerTimeRepository: TriggerTimeRepository,
    val deviceMethodsRepository: DeviceMethodsRepository,
    val openHabDeviceRepository: OpenHabDeviceRepository,
    val homeeDeviceRepository: HomeeDeviceRepository,

    val functionService: FunctionService,
    val backendController: BackendController,

    val openHabController: OpenHabController,
    val homeeController: HomeeController,
    val deviceService: DeviceService,
    val functionTypeService: FunctionTypeService

){

    private val log: Logger = LoggerFactory.getLogger(AutomationService::class.java)

    private val dateFormat = SimpleDateFormat("HH:mm:ss")

    /**
     * This method is a scheduled method.
     * The method checks all routines whether they can be executed or not.
     * Routines that can be executed are executed
     */
    @Scheduled(fixedRate = 10000)
    fun automation()
    {
        log.info("Automation: The time is now {}", dateFormat.format(Date()))
        val routinelist = routineRepository.findAll().toList()
        for (routine in routinelist)
        {
            if (routine.triggerTime != null)
            {
                val triggerTime = routine.triggerTime
                val eventtime = triggerTime!!.localTime!!
                val range = LocalTime.now().minusSeconds(10)..
                        LocalTime.now().plusSeconds(10)

                if(eventtime in range)
                {
                    if (triggerTime.repeat == true)
                    {
                        log.info("Automation: Time Event getriggert")
                        val routineEvents = routine.routineEvent
                        for (routineEvent in routineEvents) {
                            functionService.triggerFunc(
                                routineEvent.deviceId,
                                routineEvent.functionId!!,
                                routineEvent.voldemort!!
                            )
                        }
                    }
                    else if (!triggerTime.repeatExecuted)
                    {
                        triggerTime.repeatExecuted = true
                        triggerTimeRepository.save(triggerTime)
                        log.info("Automation: Ein einmaliges Time Event wurde ausgeführt")
                        val routineEvents = routine.routineEvent
                        for (routineEvent in routineEvents) {
                            functionService.triggerFunc(
                                routineEvent.deviceId,
                                routineEvent.functionId!!,
                                routineEvent.voldemort!!
                            )
                        }

                    }
                }

            }

            if( routine.triggerEventByDevice != null)
            {
                val triggerEventByDevice = routine.triggerEventByDevice
                try {


                    val deviceMethodsVal =
                        deviceMethodsRepository.findById(triggerEventByDevice!!.function.deviceMethodsId!!).get()
                    val statusDevice =
                        backendController.getMethodStatus(triggerEventByDevice.deviceId, deviceMethodsVal)
                    var triggerfunc = false
                    when (deviceMethodsVal.type) {
                        FunctionType.Switch -> triggerfunc = onOffRoutine(triggerEventByDevice, statusDevice!!)
                        FunctionType.Color -> triggerfunc =
                            rangeRoutine(routine.comparisonType!!, triggerEventByDevice, statusDevice!!)

                        FunctionType.Contact -> triggerfunc = onOffRoutine(triggerEventByDevice, statusDevice!!)
                        FunctionType.Dimmer -> triggerfunc =
                            rangeRoutine(routine.comparisonType!!, triggerEventByDevice, statusDevice!!)

                        FunctionType.Player -> triggerfunc = false
                        FunctionType.Rollershutter -> triggerfunc =
                            rangeRoutine(routine.comparisonType!!, triggerEventByDevice, statusDevice!!)

                        FunctionType.Number -> triggerfunc =
                            rangeRoutine(routine.comparisonType!!, triggerEventByDevice, statusDevice!!)

                        else -> {
                            log.error("Automation: Eine fehlerhafte Routine ")
                        }
                    }


                    if (triggerfunc) {
                        log.info("Automation: Ein trigger by Device wurde gefunden")
                        val routineEvents = routine.routineEvent
                        for (routineEvent in routineEvents) {
                            functionService.triggerFunc(
                                routineEvent.deviceId,
                                routineEvent.functionId!!,
                                routineEvent.voldemort!!
                            )
                        }
                    }
                }
                catch (ex: NoSuchElementException)
                {
                    log.error("Automation: Error Routine")
                    routineRepository.delete(routine)
                }



            }
            if(routine.triggerTime==null && routine.triggerEventByDevice == null)
            {
                log.error("Automation: Error Routine: No TriggerTime and Triggerbydevice")
                routineRepository.delete(routine)
            }

        }

    }


    private fun onOffRoutine(triggerEventByDevice: TriggerEventByDevice, status: FunctionDTO): Boolean{
        val expectedFun = triggerEventByDevice.function
        if(expectedFun.onOff != null)
        {
            if(expectedFun.onOff == status.onOff)
            {
                return true
            }
        }
        return false
    }

    private  fun rangeRoutine(comparisonType:Int, triggerEventByDevice: TriggerEventByDevice, status: FunctionDTO):Boolean
    {
        val range = triggerEventByDevice.function.range
        if(range != null) {
            if(comparisonType == 0) {
                if (range.currentValue == status.rangeDTO!!.currentValue) {
                    return true
                }

            } else if (comparisonType == 1) {
                if (range.currentValue!! < status.rangeDTO!!.currentValue) {
                    return true
                }

            } else if (comparisonType == 2) {
                if (range.currentValue!! > status.rangeDTO!!.currentValue) {
                    return true
                }
            }

        }
        return false
    }



    /**
     * updates Devices
     */
    @Scheduled(fixedRate = 900000)
    fun updateDevices()
    {
        log.info("updateDevice: Update Device at the time {}", dateFormat.format(Date()))
        val allOpenHabDevice = openHabController.getDevices()
        if(allOpenHabDevice != null)
        {
            val allOpenHabDeviveList = allOpenHabDevice.toList()
            deviceService.updateDevicesOpenHab(allOpenHabDevice)
            for (device in allOpenHabDeviveList)
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
        if(homeeController.updateNodes()) {
            val allHomeeNodes = homeeController.getNodes()
            if (allHomeeNodes != null) {
                val allNodesCopy = allHomeeNodes.toList()
                deviceService.updateNodeHomee(allHomeeNodes)
                for (node in allNodesCopy) {
                    for (att in node.attributes) {
                        functionService.saveFunctionHomee(att)
                    }
                }
            }
        }
    }




    @Scheduled(fixedRate = 3600000)
    fun removeOldDevicesAndMethods()
    {
        checkDeleteOpenHab()
        checkDeleteHomee()
        checkDeleteHomeeMethods()
        checkDeleteOpenHabMethods()
    }



    private fun checkDeleteOpenHabMethods()
    {
        val allOpenHabDevice = openHabController.getDevices()
        if (allOpenHabDevice != null) {
            val allOpenHabDeviveList = allOpenHabDevice.toList()


            for (device in allOpenHabDeviveList) {
                val allOpenHabTransformMethods = mutableListOf<DeviceMethods>()
                val channels = device.channels
                for (channel in channels) {
                    for (itemname in channel.linkedItems) {
                        val item = openHabController.getItemByName(itemname)
                        if (item != null) {
                            val functType = functionTypeService.functionsTypeOpenHab(item)
                            if (functType != null) {
                                allOpenHabTransformMethods.add(
                                    DeviceMethods(
                                        label = item.label,
                                        name = item.name,
                                        type = functType,
                                    ))
                            }

                        }
                    }
                }

                val savedDevice = openHabDeviceRepository.findOpenHabByUid(device.UID)
                for (savedMethode in savedDevice.deviceMethodsIDS)
                {
                    var found = false
                    for (methode in allOpenHabTransformMethods)
                    {
                        if (savedMethode.label == methode.label)
                        {
                            if (savedMethode.type == methode.type)
                            {
                                if(savedMethode.name == methode.name)
                                {
                                    found = true
                                }
                            }
                        }
                    }
                    if(!found)
                    {
                        log.warn("checkDeleteOpenHabMethods: Delete OpenHab Methode: ${savedMethode.name}")
                        deviceMethodsRepository.delete(savedMethode)

                    }
                }
            }
        }

    }


    private fun checkDeleteHomeeMethods()
    {

        val allHomeeNodes = homeeController.getNodes()

        if(allHomeeNodes !=null)
        {

            for(node in allHomeeNodes) {

                val deviceMethodsList = mutableListOf<DeviceMethods>()
                for (attr in node.attributes) {
                    val device = functionService.transformAttribut(attr)
                    if (device != null) {
                        deviceMethodsList.add(device)
                    }
                }
                val savedNode = homeeDeviceRepository.findHomeeByHomeeID(node.id)
                for (savedMethod in savedNode.deviceMethodsIDS) {
                    var found = false
                    for(method in deviceMethodsList)
                    {
                        if(savedMethod.label == method.label)
                        {
                            if(savedMethod.homeeattrID == method.homeeattrID)
                            {
                                if (savedMethod.type == method.type)
                                {
                                    found = true
                                }
                            }
                        }
                    }
                    if(!found)
                    {
                        log.warn("checkDeleteHomeeMethods: Delete Homee Methode: ${savedMethod.name}")
                        deviceMethodsRepository.delete(savedMethod)

                    }

                }
            }
        }
    }


    private fun checkDeleteHomee()
    {
        val allHomeeNodes:ArrayList<nodes>? = homeeController.getNodes()
        if (allHomeeNodes != null) {
            if (allHomeeNodes.isNotEmpty()) {


                val allHomeeTransformDevice = mutableListOf<HomeeDevice>()
                for (node in allHomeeNodes) {
                    allHomeeTransformDevice.add(deviceService.transformNode(node))
                }
                val allSavedHomeeDevice = homeeDeviceRepository.findAll().toList()
                for (savedDevice in allSavedHomeeDevice) {
                    var found = false
                    for (device in allHomeeTransformDevice) {
                        if (savedDevice.homeeID == device.homeeID) {
                            if (savedDevice.name == device.name) {
                                found = true

                            }
                        }
                    }
                    if (!found) {
                        log.warn("checkDeleteHomee: Delete a Homee Device: ${savedDevice.name}")
                        deviceMethodsRepository.deleteAll(savedDevice.deviceMethodsIDS.toList())
                        homeeDeviceRepository.delete(savedDevice)

                    }
                }
            }
        }


    }
    private fun checkDeleteOpenHab()
    {
        val allOpenHabDevice = openHabController.getDevices()

        if (allOpenHabDevice != null)
        {

            val allOpenHabTransformDevice = mutableListOf<OpenHabDevice>()
            for (thing in allOpenHabDevice)
            {
                allOpenHabTransformDevice.add(deviceService.transformThing(thing))
            }
            val allSavedOpenHabDevice = openHabDeviceRepository.findAll().toList()
            for (deviceSaved in allSavedOpenHabDevice)
            {
                var found = false
                for (device in allOpenHabTransformDevice)
                {
                    if (deviceSaved.uid == device.uid)
                    {
                        if (deviceSaved.name == device.name)
                        {
                            found = true
                        }
                    }
                }
                if(!found)
                {
                    log.warn("checkDeleteOpenHab: Delete a OpenHab Device: ${deviceSaved.name}")
                    deviceMethodsRepository.deleteAll(deviceSaved.deviceMethodsIDS.toList())
                    openHabDeviceRepository.delete(deviceSaved)

                }
            }
        }
    }
}