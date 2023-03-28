package de.rehatech.smartHomeBackend.services

import de.rehatech.homeekt.model.nodes
import de.rehatech.smartHomeBackend.controller.backend.BackendController
import de.rehatech.smartHomeBackend.controller.backend.HomeeController
import de.rehatech.smartHomeBackend.controller.backend.OpenHabController
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
import kotlin.collections.ArrayList


@Service
class AutomationService  @Autowired constructor(

    val routineRepository: RoutineRepository,
    val functionService: FunctionService,
    val backendController: BackendController,
    val triggerTimeRepository: TriggerTimeRepository,
    val deviceMethodsRepository: DeviceMethodsRepository,
    val openHabController: OpenHabController,
    val homeeController: HomeeController,
    val deviceService: DeviceService

){


    private val log: Logger = LoggerFactory.getLogger(AutomationService::class.java)

    private val dateFormat = SimpleDateFormat("HH:mm:ss")
    @Scheduled(fixedRate = 10000)
    fun automation()
    {
        log.info("The time is now {}", dateFormat.format(Date()))
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
                    if (triggerTime!!.repeat == true)
                    {
                        log.info("Time Event getriggert")
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
                        log.info("Ein einmaliges Time Event wurde ausgeführt")
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
                val deviceMethodsVal = deviceMethodsRepository.findById(triggerEventByDevice!!.function.deviceMethodsId!!).get()
                val statusDevice = backendController.getMethodStatus(triggerEventByDevice!!.deviceId,deviceMethodsVal )
                var triggerfunc = false
                when(deviceMethodsVal.type)
                {
                    FunctionType.Switch -> triggerfunc = onOffRoutine(triggerEventByDevice, statusDevice!!)
                    FunctionType.Color -> triggerfunc = rangeRoutine(routine.comparisonType!!, triggerEventByDevice, statusDevice!! )
                    FunctionType.Contact -> triggerfunc = onOffRoutine(triggerEventByDevice,statusDevice!!)
                    FunctionType.Dimmer -> triggerfunc = rangeRoutine(routine.comparisonType!!, triggerEventByDevice, statusDevice!! )
                    FunctionType.Player -> triggerfunc = false
                    FunctionType.Rollershutter -> triggerfunc = rangeRoutine(routine.comparisonType!!, triggerEventByDevice, statusDevice!! )
                    FunctionType.Number -> triggerfunc = rangeRoutine(routine.comparisonType!!, triggerEventByDevice, statusDevice!! )
                    else -> {log.error("Eine fehlerhafte Routine ")}
                }


                if (triggerfunc) {
                    log.info("Ein trigger by Device wurde gefunden")
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
        log.info("Update Device at the time {}", dateFormat.format(Date()))
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
}