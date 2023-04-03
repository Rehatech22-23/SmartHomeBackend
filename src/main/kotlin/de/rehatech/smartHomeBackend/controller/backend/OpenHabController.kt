package de.rehatech.smartHomeBackend.controller.backend

import com.google.gson.Gson
import de.rehatech.smartHomeBackend.config.ApiConfiguration
import de.rehatech.smartHomeBackend.response.Item
import de.rehatech.smartHomeBackend.response.Things
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
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


    /**
     * @param itemName This parameter contains the item name for OpenHab
     * @param command This parameter contains the command for the OpenHab item
     * @return
     */
    fun sendCommand(itemName: String, command: String): Boolean{
        val request = Request.Builder()
            .url("${url}/rest/items/${itemName}")
            .addHeader("Authorization","Bearer " + token) //seek seek lest
            .addHeader("Content-Type", " text/plain")
            .post(command.toRequestBody())
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(2500,TimeUnit.MILLISECONDS)
            .build()

        val respond = client.newCall(request).execute()

        return respond.isSuccessful


    }

    /**
     * The Methode return a Item by the itemName.
     * @param itemName This parameter contains the item name for OpenHab
     * @return
     */
    fun getItemByName(itemName: String): Item?
    {
        val request = Request.Builder()
            .url("${url}/rest/items/${itemName}")
            .addHeader("Authorization","Bearer " + token)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(2500,TimeUnit.MILLISECONDS)
            .build()

        val respond = client.newCall(request).execute()
        val json = respond.body?.string()
        if(respond.isSuccessful) {
            return Gson().fromJson(json, Item::class.java)
        }
        return null
    }

    /**
     * @return It will return an ArrayList of the type Things
     */
    fun getDevices():ArrayList<Things>?
    {
        val request = Request.Builder()
            .url("${url}/rest/things")
            .addHeader("Authorization","Bearer " + token)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(2500,TimeUnit.MILLISECONDS)
            .build()

        val respond = client.newCall(request).execute()
        val json = respond.body?.string()
        if(respond.isSuccessful) {
            val arr = Gson().fromJson(json, Array<Things>::class.java)
            val arrayList = ArrayList<Things>()
            Collections.addAll(arrayList, *arr)
            return arrayList
        }
        return null
    }

    /** //TODO Docs
     * @param uid
     * @return
     */
    fun getDevice(uid:String): Things?
    {
        val request = Request.Builder()
            .url("${url}/rest/things/${uid}")
            .addHeader("Authorization","Bearer " + token)
            .build()

        val client = OkHttpClient.Builder()

            .connectTimeout(2500,TimeUnit.MILLISECONDS)
            .build()

        val respond = client.newCall(request).execute()
        val json = respond.body?.string()
        if(respond.isSuccessful) {
            val arr = Gson().fromJson(json, Things::class.java)
            return arr
        }
        return null
    }
}