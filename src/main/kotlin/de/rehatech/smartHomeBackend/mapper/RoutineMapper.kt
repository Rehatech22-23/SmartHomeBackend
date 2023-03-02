package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.Routine
import de.rehatech2223.datamodel.RoutineDTO

class RoutineMapper {

    companion object {
        fun mapToEntity(routineDTO: RoutineDTO): Routine {
            var result = Routine()
            if (routineDTO.triggerEventByDeviceDTO == null) {
                result.id = routineDTO.routineId
                result.routineName = routineDTO.routineName
                result.triggerType = routineDTO.triggerType
                result.triggerTime = TriggerTimeMapper.mapToEntity(routineDTO.triggerTime!!)
                result.routineEvent = RoutineEventMapper.mapToEntityMutableList(routineDTO.routineEventDTO)
            }else { // routineDTO.triggerTimeDTO == null
                result.id = routineDTO.routineId
                result.routineName = routineDTO.routineName
                result.triggerType = routineDTO.triggerType
                result.triggerEventByDevice = TriggerEventByDeviceMapper.mapToEntity(routineDTO.triggerEventByDeviceDTO!!)
                result.routineEvent = RoutineEventMapper.mapToEntityMutableList(routineDTO.routineEventDTO)
            }
            return result
        }

        fun mapToDTO(routine: Routine): RoutineDTO {
            if (routine.triggerEventByDevice == null) {
                return RoutineDTO.Builder(
                    routine.routineName,
                    routine.triggerType!!,
                    RoutineEventMapper.mapToDTOList(routine.routineEvent),
                    null,
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
                    routine.triggerType!!,
                    RoutineEventMapper.mapToDTOList(routine.routineEvent),
                    null,
                    null,
                    null
                ).routineId(routine.id!!)
                    .triggerEventByDeviceDTO(TriggerEventByDeviceMapper.mapToDTO(routine.triggerEventByDevice!!))
                    .build()
            }
        }
    }
}