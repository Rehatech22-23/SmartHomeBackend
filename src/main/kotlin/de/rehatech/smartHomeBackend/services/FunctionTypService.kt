package de.rehatech.smartHomeBackend.services

import de.rehatech.homeekt.model.attributes
import de.rehatech.smartHomeBackend.enum.FunctionType
import de.rehatech.smartHomeBackend.response.Item
import org.springframework.stereotype.Service

@Service
class FunctionTypService {

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
            itemstring = "StringType"
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