package de.rehatech.smartHomeBackend.controller.frontend

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/function")
class FunctionController {

        //returns a function with same FunctionId as the parameter
        @GetMapping()
        fun functionByFunctionId(@RequestParam functionId: Long?): de.rehatech2223.datamodel.FunctionDTO? {
            return null
        }

        //"triggers a function", 200 ok 500 Internal Server Error
        @PutMapping("/trigger")
        fun triggerFunc(@RequestParam deviceId: String?, @RequestParam functionId: Long?, @RequestBody body: String){
        }



}