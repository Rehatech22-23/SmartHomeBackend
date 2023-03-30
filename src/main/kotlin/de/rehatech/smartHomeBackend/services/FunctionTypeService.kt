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
     * //TODO: Docs
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
     * //TODO: Docs
     * @param item
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
            if ("Philips_AirPurifier_Air" in item.name)
            {
                itemstring = "Air"
            }
            else {
                itemstring = "StringType"
            }
        }
        try {
            val test = FunctionType.valueOf(itemstring)
            return test
        }
        catch (  e:IllegalArgumentException      )
        {
            return null
        }



    }
}