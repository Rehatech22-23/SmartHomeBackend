package de.rehatech.smartHomeBackend.controller.backend.responsesClass

import com.google.gson.Gson
import de.rehatech.smartHomeBackend.response.Things
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class ThingsTest
{
    @Test
    fun thingsTest()
    {
        val path = System.getProperty("user.dir")

        println("Working Directory = $path")

        val filePath = "src/test/kotlin/de/rehatech/smartHomeBackend/controller/backend/responsesClass/json/things.json"

        var content = Files.readString(Paths.get(filePath), Charsets.UTF_8)
        println(content)

        val item = Gson().fromJson(content, Array<Things>::class.java);
        println(item)
    }
}