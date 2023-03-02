package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.RoutineEvent
import de.rehatech2223.datamodel.util.RoutineEventDTO

class RoutineEventMapper {

    companion object{
        fun mapToEntity(routineEventDTO: RoutineEventDTO): RoutineEvent {
            val result = RoutineEvent()
            result.id = routineEventDTO.routineEventId
            //result.routineId = routineEventDTO.routineId
            result.deviceId = routineEventDTO.deviceId
            result.functionId = routineEventDTO.functionId
            result.voldemort = routineEventDTO.functionValue
            return result
        }

        fun mapToDTO(routineEvent: RoutineEvent): RoutineEventDTO {
            return RoutineEventDTO(
                routineEvent.deviceId,
                routineEvent.functionId!!,
                routineEvent.voldemort!!,
                //routineEvent.routineId,
                routineEvent.id
            )
        }

        fun mapToEntityMutableList(dtoList: ArrayList<RoutineEventDTO>): MutableList<RoutineEvent> {
            val result = ArrayList<RoutineEvent>()
            for (dto in dtoList) {
                result.add(mapToEntity(dto))
            }
            return result
        }

        fun mapToDTOList(entityList: MutableList<RoutineEvent>):  ArrayList<RoutineEventDTO> {
            val result = ArrayList<RoutineEventDTO>()
            for (entity in entityList) {
                result.add(mapToDTO(entity))
            }
            return result
        }
    }
}