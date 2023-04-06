package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.Routine
import de.rehatech2223.datamodel.RoutineDTO

/**
 * A mapper that maps RoutineDTOs to RoutineEntities and vice versa
 */
class RoutineMapper {

    companion object {
        fun mapToEntity(routineDTO: RoutineDTO): Routine {
            val result = Routine()

            result.id = routineDTO.routineId
            result.routineName = routineDTO.routineName
            result.comparisonType = routineDTO.comparisonType
            result.routineEvent = RoutineEventMapper.mapToEntityMutableList(routineDTO.routineEventDTO)

            if (routineDTO.triggerEventByDeviceDTO != null) {
                result.triggerEventByDevice =
                        TriggerEventByDeviceMapper.mapToEntity(routineDTO.triggerEventByDeviceDTO!!)
            } else if (routineDTO.triggerTime != null) { // routineDTO.triggerTimeDTO != null
                result.triggerTime = TriggerTimeMapper.mapToEntity(routineDTO.triggerTime!!)
            }

            return result
        }

        fun mapToDTO(routine: Routine): RoutineDTO {
            if (routine.triggerEventByDevice != null) {
                return RoutineDTO.Builder(
                        routine.routineName,
                        routine.comparisonType!!,
                        RoutineEventMapper.mapToDTOList(routine.routineEvent),
                        routine.id!!,
                        null,
                        null
                ).triggerEventByDeviceDTO(TriggerEventByDeviceMapper.mapToDTO(routine.triggerEventByDevice!!)).build()
            } else if (routine.triggerTime != null) {
                return RoutineDTO.Builder(
                        routine.routineName,
                        routine.comparisonType!!,
                        RoutineEventMapper.mapToDTOList(routine.routineEvent),
                        routine.id!!,
                        null,
                        null
                ).triggerTime(TriggerTimeMapper.mapToDTO(routine.triggerTime!!)).build()
            }else {
                return RoutineDTO.Builder(
                        routine.routineName,
                        routine.comparisonType!!,
                        RoutineEventMapper.mapToDTOList(routine.routineEvent),
                        routine.id!!,
                        null,
                        null
                ).build()
            }
        }
    }
}