package app.gmaaps.pump.omnipod.common.bledriver.pod.response

import app.gmaaps.pump.omnipod.common.bledriver.pod.response.ResponseType.StatusResponseType

open class AdditionalStatusResponseBase internal constructor(
    val statusResponseType: StatusResponseType,
    encoded: ByteArray
) : ResponseBase(ResponseType.ADDITIONAL_STATUS_RESPONSE, encoded)

