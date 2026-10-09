package app.gmaaps.pump.omnipod.common.bledriver.pod.response

import app.gmaaps.pump.omnipod.common.bledriver.pod.response.ResponseType.ActivationResponseType

abstract class ActivationResponseBase(
    val activationResponseType: ActivationResponseType,
    encoded: ByteArray
) : ResponseBase(ResponseType.ACTIVATION_RESPONSE, encoded)

