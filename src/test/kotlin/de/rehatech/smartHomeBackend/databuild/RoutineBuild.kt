package de.rehatech.smartHomeBackend.databuild

import de.rehatech.smartHomeBackend.entities.Range
import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech2223.datamodel.RoutineDTO
import de.rehatech2223.datamodel.util.RangeDTO
import de.rehatech2223.datamodel.util.RoutineEventDTO
import de.rehatech2223.datamodel.util.TriggerEventByDeviceDTO
import de.rehatech2223.datamodel.util.TriggerTimeDTO
import java.time.LocalTime

class RoutineBuild {

    fun sameRoutine():RoutineDTO
    {
        return RoutineDTO.Builder("RoutineTest", 0,
            eventList(),-1, triggerEventByDeviceDTO =  triggerEventRange()).build()
    }

    fun littleRoutine():RoutineDTO
    {
        return RoutineDTO.Builder("RoutineTest", 1,
            eventList(),-1, triggerEventByDeviceDTO =  triggerEventRange()).build()
    }

    fun biggerRoutine():RoutineDTO
    {
        return RoutineDTO.Builder("RoutineTest", 2,
            eventList(),-1, triggerEventByDeviceDTO =  triggerEventRange()).build()
    }

    fun noRangeRoutine():RoutineDTO
    {
        return RoutineDTO.Builder("RoutineTest", 0,
            eventList(),-1, triggerEventByDeviceDTO =  triggerEvent()).build()
    }

    fun triggertimeRoutine():RoutineDTO
    {
        return RoutineDTO.Builder("RoutineTest", 0, eventList(),-1, triggerTime()).build()
    }

    fun triggertimeRoutineNoRepeat():RoutineDTO
    {
        return RoutineDTO.Builder("RoutineTest", 0, eventList(),-1, triggerTimeRepeatFalse()).build()
    }
    fun triggertimeBigRangeRoutine():RoutineDTO
    {
        return RoutineDTO.Builder("RoutineTest", 0, eventList(),-1, triggerTimeBigRange()).build()
    }

    fun errorRoutine():RoutineDTO
    {
        return RoutineDTO.Builder("RoutineTest", 0, eventList(),-1).build()
    }
    fun eventList():ArrayList<RoutineEventDTO>
    {
        val routineEventDTO = arrayListOf<RoutineEventDTO>()
        val mockevent = RoutineEventDTO("HM:1",1,1F)
        routineEventDTO.add(mockevent)
        return routineEventDTO
    }

    private fun rangeTest(): RangeDTO
    {
        val minValue = 0.0
        val maxValue = 1.0
        val currentValue = 0.5
        return RangeDTO(minValue, maxValue, currentValue)
    }

    private  fun triggerTimeBigRange():TriggerTimeDTO
    {
        val time = LocalTime.of(4,2,3)
        val repeat = true
        val triggerTimeId = 0L
        return TriggerTimeDTO(time, repeat, triggerTimeId, null)
    }
    private  fun triggerTime():TriggerTimeDTO
    {
        val time = LocalTime.now()
        val repeat = true
        val triggerTimeId = 0L
        return TriggerTimeDTO(time, repeat, triggerTimeId, null)
    }
    private  fun triggerTimeRepeatFalse():TriggerTimeDTO
    {
        val time = LocalTime.now()
        val repeat = false
        val triggerTimeId = 0L
        return TriggerTimeDTO(time, repeat, triggerTimeId, null)
    }
    private  fun triggerEventRange ():TriggerEventByDeviceDTO
    {
        return TriggerEventByDeviceDTO("OH:1", functionWithRangeTest(), 1, null)
    }

    private  fun triggerEvent ():TriggerEventByDeviceDTO
    {
        return TriggerEventByDeviceDTO("OH:1", functionNotRangeTest(), 1, null)
    }
    private fun functionWithRangeTest(): FunctionDTO
    {
        val onOff = true
        val outputValue = "Some output value"
        val outputTrigger = true
        return FunctionDTO.Builder("Heizung", 1, rangeTest(), onOff, outputValue, outputTrigger).build()

    }

    private fun functionNotRangeTest(): FunctionDTO
    {
        val onOff = true
        val outputValue = "Some output value"
        val outputTrigger = true
        return FunctionDTO.Builder("Heizung", 1, null, onOff, outputValue, outputTrigger).build()

    }
}