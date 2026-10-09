package app.gmaaps.core.nssdk.mapper

import app.gmaaps.core.nssdk.localmodel.Storage
import app.gmaaps.core.nssdk.remotemodel.RemoteStorage

internal fun RemoteStorage.toLocal() = Storage(storage = storage, version = version)

