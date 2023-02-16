package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.Routine
import de.rehatech2223.datamodel.RoutineDTO

class RoutineMapper {

    companion object {
        fun mapToEntity(routineDTO: RoutineDTO): Routine{
            var result = Routine()
            result.id = routineDTO.routineId
            result.routineName = routineDTO.routineName
            result.triggerType = routineDTO.triggerType
            result.triggerTime = TriggerTimeMapper.mapToEntity(routineDTO.triggerTime!!)
            result.triggerEventByDevice = TriggerEventByDeviceMapper.mapToEntity(routineDTO.triggerEventByDeviceDTO!!)
            result.routineEvent = RoutineEventMapper.mapToEntityArrayList(routineDTO.routineEventDTO)
            return result
        }

        fun mapToDTO(routine: Routine): RoutineDTO {
            return RoutineDTO(
                routine.routineName,
                routine.id!!,
                routine.triggerType!!,
                TriggerTimeMapper.mapToDTO(routine.triggerTime!!),
                TriggerEventByDeviceMapper.mapToDTO(routine.triggerEventByDevice!!),
                RoutineEventMapper.mapToDTOArrayList(routine.routineEvent)
            );
        }
    }
}