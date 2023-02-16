package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.TriggerTime
import de.rehatech2223.datamodel.util.TriggerTimeDTO

class TriggerTimeMapper {

    companion object{
        fun mapToEntity(triggerTimeDTO: TriggerTimeDTO): TriggerTime{
        val result = TriggerTime()
        result.id = triggerTimeDTO.triggerTimeID
        result.localTime = triggerTimeDTO.time
        result.repeat = triggerTimeDTO.repeat
        result.routineId = triggerTimeDTO.routineID
        return result
    }

        fun mapToDTO(triggerTime: TriggerTime): TriggerTimeDTO {
            return TriggerTimeDTO(
                triggerTime.id,
                triggerTime.routineId,
                triggerTime.localTime!!,
                triggerTime.repeat!!
            );
        }}
}