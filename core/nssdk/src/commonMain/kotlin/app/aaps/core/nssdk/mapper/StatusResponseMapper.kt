package app.gmaaps.core.nssdk.mapper

import app.gmaaps.core.nssdk.localmodel.Status
import app.gmaaps.core.nssdk.remotemodel.RemoteStatusResponse

internal fun RemoteStatusResponse.toLocal() = Status(
    version = version,
    apiVersion = apiVersion,
    srvDate = srvDate,
    storage = storage.toLocal(),
    apiPermissions = apiPermissions.toLocal()
)

