package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.RoutineEvent
import de.rehatech2223.datamodel.util.RoutineEventDTO

class RoutineEventMapper {

    companion object{
        fun mapToEntity(routineEventDTO: RoutineEventDTO): RoutineEvent {
            val result = RoutineEvent()
            result.id = routineEventDTO.routineEventID
            result.routineId = routineEventDTO.routineID
            result.deviceId = routineEventDTO.deviceId
            result.functionId = routineEventDTO.functionId
            result.voldemort = routineEventDTO.functionValue
            return result
        }

        fun mapToDTO(routineEvent: RoutineEvent): RoutineEventDTO {
            return RoutineEventDTO(
                routineEvent.id,
                routineEvent.routineId,
                routineEvent.deviceId,
                routineEvent.functionId!!,
                routineEvent.voldemort!!
            )
        }

        fun mapToEntityArrayList(dtoList: ArrayList<RoutineEventDTO>): ArrayList<RoutineEvent> {
            val result = ArrayList<RoutineEvent>()
            for (dto in dtoList) {
                result.add(RoutineEventMapper.mapToEntity(dto))
            }
            return result
        }

        fun mapToDTOArrayList(entityList: ArrayList<RoutineEvent>): ArrayList<RoutineEventDTO> {
            val result = ArrayList<RoutineEventDTO>()
            for (entity in entityList) {
                result.add(mapToDTO(entity))
            }
            return result
        }
    }
}