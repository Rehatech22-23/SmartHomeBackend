package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.TriggerTime
import de.rehatech2223.datamodel.util.TriggerTimeDTO

class TriggerTimeMapper {

    companion object{
        fun mapToEntity(triggerTimeDTO: TriggerTimeDTO): TriggerTime{
        val result = TriggerTime()
        result.id = triggerTimeDTO.triggerTimeId
        result.localTime = triggerTimeDTO.time
        result.repeat = triggerTimeDTO.repeat
        result.routineId = triggerTimeDTO.routineId
        return result
    }

        fun mapToDTO(triggerTime: TriggerTime): TriggerTimeDTO {
            return TriggerTimeDTO(
                triggerTime.localTime!!,
                triggerTime.repeat!!,
                triggerTime.id,
                triggerTime.routineId
            )
        }}
}