package de.rehatech.smartHomeBackend.mapper

import de.rehatech.smartHomeBackend.entities.Range
import de.rehatech2223.datamodel.util.RangeDTO

/**
 * A mapper that maps RangeDTOs to RangeEntities and vice versa
 *
 * @author Tim Bräuker
 */
class RangeMapper {

    companion object {
         fun mapToEntity(rangeDTO: RangeDTO): Range {
             val result = Range()
             result.maxValue = rangeDTO.maxValue
             result.minValue = rangeDTO.minValue
             result.currentValue = rangeDTO.currentValue
             return result
         }

        fun mapToDTO(range: Range): RangeDTO {
            return RangeDTO(range.minValue!!,
                range.maxValue!!,
                range.currentValue!!)
        }
    }
}