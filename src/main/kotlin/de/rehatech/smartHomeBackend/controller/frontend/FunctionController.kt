package de.rehatech.smartHomeBackend.controller.frontend

import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech.smartHomeBackend.services.FunctionService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * A class that handles API calls for DeviceMethod Objects
 *
 * @param functionService Instance gets automatically autowired into the Controller
 */
@RestController
@RequestMapping("/function")
class FunctionController @Autowired constructor(val functionService: FunctionService){

        private val log: Logger = LoggerFactory.getLogger(FunctionController::class.java)

    //returns a function with same FunctionId as the parameter
        @GetMapping
        fun functionByFunctionId(@RequestParam functionId: Long): FunctionDTO? {
            try {
                    return functionService.getFunction(functionId)
            } catch (ex: NullPointerException){
                log.error("Funktion with id: $functionId was not  retrieved successfully")
                throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.localizedMessage, ex)
            } catch (ex: IllegalArgumentException){
                log.error("illegal value in request body")
                throw ResponseStatusException(HttpStatus.NO_CONTENT, ex.localizedMessage, ex)
            } catch (ex: NoSuchMethodError){
                log.error("There is no Homee Device which has this Functiontype")
                throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, ex.localizedMessage, ex)
            } catch (ex: NoSuchElementException) {
                log.error("Function with id: $functionId does not  exist in Database")
                throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, ex.localizedMessage, ex)

            }
        }

        //"triggers a function"
        @PutMapping("/trigger")
        fun triggerFunc(@RequestParam deviceId: String, @RequestParam functionId: Long, @RequestBody body: Float){
                try {
                    functionService.triggerFunc(deviceId, functionId, body)
                } catch (ex: NullPointerException){
                    log.error("Funktion with id: $functionId was not  trigger successfully")
                    throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.localizedMessage, ex)
                }
        }



}