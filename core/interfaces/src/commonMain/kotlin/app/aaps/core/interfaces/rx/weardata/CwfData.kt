package app.gmaaps.core.interfaces.rx.weardata

import kotlinx.serialization.Serializable

@Serializable
data class CwfData(val json: String, var metadata: CwfMetadataMap, val resData: CwfResDataMap)
