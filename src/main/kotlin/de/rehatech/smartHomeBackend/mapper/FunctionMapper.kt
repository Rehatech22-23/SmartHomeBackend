package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.Function
import de.rehatech2223.datamodel.FunctionDTO

class FunctionMapper {

    companion object{
        fun mapToEntity(functionDTO: FunctionDTO): Function {
            val result = Function()
            result.id = functionDTO.functionId
            result.functionName = functionDTO.functionName
            result.range = RangeMapper.mapToEntity(functionDTO.rangeDTO!!)
            result.onOff = functionDTO.onOff
            result.outputValue = functionDTO.outputValue
            result.outputTrigger = functionDTO.outputTrigger
            return  result
        }

        fun mapToDTO(function: Function): FunctionDTO {
            return FunctionDTO.Builder(
                function.functionName,
                function.id!!,
                ).rangeDTO(RangeMapper.mapToDTO(function.range!!))
                .onOff(function.onOff!!)
                .outputValue(function.outputValue!!)
                .outputTrigger(function.outputTrigger!!)
                .build()
        }
    }
}