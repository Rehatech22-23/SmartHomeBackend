package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.TriggerEventByDevice
import de.rehatech2223.datamodel.util.TriggerEventByDeviceDTO

class TriggerEventByDeviceMapper {

    companion object{

        fun mapToEntity(triggerEventByDeviceDTO: TriggerEventByDeviceDTO): TriggerEventByDevice {
            val result = TriggerEventByDevice()
            result.id = triggerEventByDeviceDTO.triggerEventByDeviceID
            result.deviceId = triggerEventByDeviceDTO.deviceId
            result.routineID = triggerEventByDeviceDTO.routineID
            result.function = FunctionMapper.mapToEntity(triggerEventByDeviceDTO.functionDTOExpectation)
            return result
        }

        fun mapToDTO(triggerEventByDevice: TriggerEventByDevice): TriggerEventByDeviceDTO {
            return TriggerEventByDeviceDTO(
                triggerEventByDevice.id,
                triggerEventByDevice.routineID,
                triggerEventByDevice.deviceId,
                FunctionMapper.mapToDTO(triggerEventByDevice.function)
            )
        }
    }
}