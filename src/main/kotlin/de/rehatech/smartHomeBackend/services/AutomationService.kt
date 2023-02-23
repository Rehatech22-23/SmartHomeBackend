package de.rehatech.smartHomeBackend.services

import de.rehatech.smartHomeBackend.controller.backend.BackendController
import de.rehatech.smartHomeBackend.repositories.*
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*


@Service
class AutomationService  @Autowired constructor(

    val routineRepository: RoutineRepository,
    val functionService: FunctionService,
    val backendController: BackendController,
    val triggerTimeRepository: TriggerTimeRepository,
    val deviceMethodsRepository: DeviceMethodsRepository

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
                val event = routine.triggerTime
                val eventtime = event!!.localTime!!.toLocalTime()
                val range = LocalTime.of(LocalTime.now().hour,LocalTime.now().minute, LocalTime.now().second-10)..
                        LocalTime.of(LocalTime.now().hour,LocalTime.now().minute, LocalTime.now().second+10)

                if(eventtime in range)
                {
                    if (event!!.repeat == true)
                    {
                        log.info("Time Event getriggert")
                    }
                    else if (event.repeatExecuted == false)
                    {
                        event.repeatExecuted = true
                        triggerTimeRepository.save(event)
                        log.info("Ein einmaliges Time Event wurde ausgeführt")

                    }
                    val routineEvents= routine.routineEvent
                    for (routineEvent in routineEvents)
                    {
                        functionService.triggerFunc(routineEvent.deviceId, routineEvent.functionId!!, routineEvent.voldemort!!)

                    }
                }

            }

            if( routine.triggerEventByDevice != null)
            {
                val event = routine.triggerEventByDevice
                val statusDevice = backendController.getMethodStatus(event!!.deviceId, deviceMethodsRepository.findById(event.function.deviceMethodsId!!).get() )
                if (functionService.equals(statusDevice!!, event.function))
                {
                    log.info("Ein trigger by Device wurde gefunden")
                    val routineEvents= routine.routineEvent
                    for (routineEvent in routineEvents)
                    {
                        functionService.triggerFunc(routineEvent.deviceId, routineEvent.functionId!!, routineEvent.voldemort!!)
                    }
                }



            }

        }

    }
}