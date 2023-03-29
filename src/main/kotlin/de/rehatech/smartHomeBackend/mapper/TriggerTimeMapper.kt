package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.TriggerTime
import de.rehatech2223.datamodel.util.TriggerTimeDTO

/**
 * A mapper that maps TriggerTimeDTOs to TriggerTimeEntities and vice versa
 *
 * @author Tim Bräuker
 */
class TriggerTimeMapper {

    companion object{
        fun mapToEntity(triggerTimeDTO: TriggerTimeDTO): TriggerTime{
        val result = TriggerTime()
        result.id = triggerTimeDTO.triggerTimeId
        result.localTime = triggerTimeDTO.time
        result.repeat = triggerTimeDTO.repeat
        return result
    }

        fun mapToDTO(triggerTime: TriggerTime): TriggerTimeDTO {
            return TriggerTimeDTO(
                triggerTime.localTime!!,
                triggerTime.repeat!!,
                triggerTime.id,
            )
        }}
}