package de.rehatech.smartHomeBackend.services

import de.rehatech.homeekt.model.attributes
import de.rehatech.smartHomeBackend.enums.FunctionType
import de.rehatech.smartHomeBackend.response.Item
import org.springframework.stereotype.Service

/**
 * A Service class handling the automated execution of Routines
 */
@Service
class FunctionTypeService {

    /**
     * Sets the FunktionType of a Homee Device.
     * @param attribute referencing the attributes data class of our homeekt library
     * @return FunctionType?
     */
    fun functionsTypeHomee(attribute: attributes): FunctionType?
    {
        when(attribute.type){
            1 -> return FunctionType.Switch
            2 -> return FunctionType.Dimmer
            23 -> return FunctionType.Color

            33 -> return null // Keine Ahnung was für eine Funktion das ist
            42 -> return FunctionType.Color

            45 -> return null
            124 -> return FunctionType.Color

            133 -> return null

            170 -> return null


            385 -> return null

        }
        return null

    }

    /**
     * Sets the FunktionType of a OpenHab Device.
     * @param item OpenHab Item
     * @return FunctionType?
     */
    fun functionsTypeOpenHab(item: Item): FunctionType?
    {
        var itemstring = item.type

        if (itemstring.contains(":"))
        {
            val erg= itemstring.split(":")
            itemstring = erg[0]
        }
        if(itemstring.contains("String"))
        {
            itemstring =
                if ("Philips_AirPurifier_Air" in item.name) {
                    "Air"
                } else {
                    "StringType"
                }
        }
        return try {
            FunctionType.valueOf(itemstring)
        } catch (e: IllegalArgumentException) {
            null
        }



    }
}