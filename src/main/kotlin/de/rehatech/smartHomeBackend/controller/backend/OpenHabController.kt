package de.rehatech.smartHomeBackend.controller.backend

import com.google.gson.Gson
import de.rehatech.smartHomeBackend.config.ApiConfiguration
import de.rehatech.smartHomeBackend.response.Item
import de.rehatech.smartHomeBackend.response.Things
import de.rehatech.smartHomeBackend.services.AutomationService
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.net.UnknownHostException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * A class that handles commands dor OpenHabDevices
 *
 * @param apiConfiguration Instance gets automatically autowired into the Controller
 */
@Component
class OpenHabController @Autowired constructor (

    apiConfiguration: ApiConfiguration
)
{
    val url = apiConfiguration.openhabUrl
    val token = apiConfiguration.openhabToken

    private val log: Logger = LoggerFactory.getLogger(OpenHabController::class.java)

    private val dateFormat = SimpleDateFormat("HH:mm:ss")


    /**
     * send a Command to OpenHab
     * @param itemName This parameter contains the item name for OpenHab
     * @param command This parameter contains the command for the OpenHab item
     * @return
     */
    fun sendCommand(itemName: String, command: String): Boolean{
        try {
            val request = Request.Builder()
                .url("${url}/rest/items/${itemName}")
                .addHeader("Authorization", "Bearer " + token) //seek seek lest
                .addHeader("Content-Type", " text/plain")
                .post(command.toRequestBody())
                .build()

            val client = OkHttpClient.Builder()
                .connectTimeout(2500, TimeUnit.MILLISECONDS)
                .build()

            val respond = client.newCall(request).execute()

            return respond.isSuccessful
        }
        catch (ex: UnknownHostException)
        {
            log.error("Der Host vom OpenHab wurde nicht gefunden.")
            return false
        }


    }

    /**
     * The Methode return a Item by the itemName.
     * @param itemName This parameter contains the item name for OpenHab
     * @return
     */
    fun getItemByName(itemName: String): Item?
    {
        try {


            val request = Request.Builder()
                .url("${url}/rest/items/${itemName}")
                .addHeader("Authorization", "Bearer " + token)
                .build()

            val client = OkHttpClient.Builder()
                .connectTimeout(2500, TimeUnit.MILLISECONDS)
                .build()

            val respond = client.newCall(request).execute()
            val json = respond.body?.string()
            if (respond.isSuccessful) {
                return Gson().fromJson(json, Item::class.java)
            }
            return null
        }
        catch (ex: UnknownHostException)
        {
            log.error("Der Host vom OpenHab wurde nicht gefunden.")
            return null
        }
    }

    /**
     * send a command to get a List with Things
     * @return It will return an ArrayList of the type Things
     */
    fun getDevices():ArrayList<Things>?
    {
        try {


            val request = Request.Builder()
                .url("${url}/rest/things")
                .addHeader("Authorization", "Bearer " + token)
                .build()

            val client = OkHttpClient.Builder()
                .connectTimeout(2500, TimeUnit.MILLISECONDS)
                .build()

            val respond = client.newCall(request).execute()
            val json = respond.body?.string()
            if (respond.isSuccessful) {
                val arr = Gson().fromJson(json, Array<Things>::class.java)
                val arrayList = ArrayList<Things>()
                Collections.addAll(arrayList, *arr)
                return arrayList
            }
            return null
        }
        catch (ex: UnknownHostException)
        {
            log.error("Der Host vom OpenHab wurde nicht gefunden.")
            return null
        }
    }

    /**
     * get a Thing from uid
     * @param uid String of the device name
     * @return Thing
     */
    fun getDevice(uid:String): Things?
    {
        try {
            val request = Request.Builder()
                .url("${url}/rest/things/${uid}")
                .addHeader("Authorization", "Bearer " + token)
                .build()

            val client = OkHttpClient.Builder()

                .connectTimeout(2500, TimeUnit.MILLISECONDS)
                .build()

            val respond = client.newCall(request).execute()
            val json = respond.body?.string()
            if (respond.isSuccessful) {
                val arr = Gson().fromJson(json, Things::class.java)
                return arr
            }
            return null
        }catch (ex: UnknownHostException)
        {
            log.error("Der Host vom OpenHab wurde nicht gefunden.")
            return null
        }
    }
}