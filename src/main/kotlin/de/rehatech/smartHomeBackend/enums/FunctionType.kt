package de.rehatech.smartHomeBackend.enums

/**
 * An Enum class stating all FunctionTypes the application deals with
 * @param typ String that determines the FunctionTpye
 */
enum class FunctionType(val typ: String) {
    Switch("Switch"),
    Call("Call"),
    Color("Color"),
    Contact("Contact"),
    Datetime("DateTime"),
    Dimmer("Dimmer"),
    Group("Group"),
    Image("Image"),
    Location("Location"),
    Number("Number"),
    Player("Player"),
    Rollershutter("Rollershutter"),
    StringType("String"),
    Air("Air")


}