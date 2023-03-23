package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.Routine
import de.rehatech2223.datamodel.RoutineDTO

class RoutineMapper {

    companion object {
        fun mapToEntity(routineDTO: RoutineDTO): Routine {
            val result = Routine()
            if (routineDTO.triggerEventByDeviceDTO == null) {
                result.id = null
                result.routineName = routineDTO.routineName
                result.comparisonType  = routineDTO.comparisonType
                result.triggerTime = TriggerTimeMapper.mapToEntity(routineDTO.triggerTime!!)
                result.routineEvent = RoutineEventMapper.mapToEntityMutableList(routineDTO.routineEventDTO)
            } else { // routineDTO.triggerTimeDTO == null
                result.id = null
                result.routineName = routineDTO.routineName
                result.comparisonType  = routineDTO.comparisonType
                result.triggerEventByDevice =
                    TriggerEventByDeviceMapper.mapToEntity(routineDTO.triggerEventByDeviceDTO!!)
                result.routineEvent = RoutineEventMapper.mapToEntityMutableList(routineDTO.routineEventDTO)
            }
            return result
        }

        fun mapToDTO(routine: Routine): RoutineDTO {
            if (routine.triggerEventByDevice == null) {
                return RoutineDTO.Builder(
                    routine.routineName,
                    routine.comparisonType!!,
                    RoutineEventMapper.mapToDTOList(routine.routineEvent),
                    routine.id!!,
                    null,
                    null
                ).routineId(routine.id!!)
                    .triggerTime(
                        TriggerTimeMapper
                            .mapToDTO(routine.triggerTime!!)
                    )
                    .build()
            } else {
                return RoutineDTO.Builder(
                    routine.routineName,
                    routine.comparisonType!!,
                    RoutineEventMapper.mapToDTOList(routine.routineEvent),
                    routine.id!!,
                    null,
                    null
                ).routineId(routine.id!!)
                    .triggerEventByDeviceDTO(TriggerEventByDeviceMapper.mapToDTO(routine.triggerEventByDevice!!))
                    .build()
            }
        }
    }
}