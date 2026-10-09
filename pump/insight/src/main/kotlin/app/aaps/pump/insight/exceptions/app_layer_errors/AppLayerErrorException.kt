package app.gmaaps.pump.insight.exceptions.app_layer_errors

abstract class AppLayerErrorException(val errorCode: Int) : app.gmaaps.pump.insight.exceptions.AppLayerException() {

    override val message: String
        get() = "Error code: $errorCode"
}
