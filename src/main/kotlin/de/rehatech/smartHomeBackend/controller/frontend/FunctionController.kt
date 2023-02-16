package de.rehatech.smartHomeBackend.controller.frontend

import de.rehatech2223.datamodel.FunctionDTO
import de.rehatech.smartHomeBackend.services.FunctionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/function")
class FunctionController @Autowired constructor(val functionService: FunctionService){

        //returns a function with same FunctionId as the parameter
        @GetMapping()
        fun functionByFunctionId(@RequestParam functionId: Long): FunctionDTO? {
            try {
                    return functionService.getFunction(functionId)
            } catch (ex: NullPointerException){
                    throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.localizedMessage, ex)
            }
        }

        //"triggers a function"
        @PutMapping("/trigger")
        fun triggerFunc(@RequestParam deviceId: String, @RequestParam functionId: Long, @RequestBody body: Float){
                try {
                    functionService.triggerFunc(deviceId, functionId, body)
                } catch (ex: NullPointerException){
                        throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.localizedMessage, ex)
                }
        }



}