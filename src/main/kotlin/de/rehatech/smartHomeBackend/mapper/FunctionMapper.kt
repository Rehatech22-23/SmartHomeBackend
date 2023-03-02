package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.Function
import de.rehatech2223.datamodel.FunctionDTO

class FunctionMapper {

    companion object {
        fun mapToEntity(functionDTO: FunctionDTO): Function {
            val result = Function()
            result.id = functionDTO.functionId
            result.functionName = functionDTO.functionName
            if (functionDTO.rangeDTO != null) result.range = RangeMapper.mapToEntity(functionDTO.rangeDTO!!)
            result.onOff = functionDTO.onOff
            result.outputValue = functionDTO.outputValue
            result.outputTrigger = functionDTO.outputTrigger
            return result
        }

        fun mapToDTO(function: Function): FunctionDTO {
            var functionDTOBuilder = FunctionDTO.Builder(
                function.functionName,
                function.id!!,
            )
            if (function.range != null) functionDTOBuilder.rangeDTO(RangeMapper.mapToDTO(function.range!!))
            if (function.onOff != null) functionDTOBuilder.onOff(function.onOff!!)
            if (function.outputValue != null) functionDTOBuilder.outputValue(function.outputValue!!)
            if (function.outputTrigger != null) functionDTOBuilder.outputTrigger(function.outputTrigger!!)
            return functionDTOBuilder.build();
        }
    }
}