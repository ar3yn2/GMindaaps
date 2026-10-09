package app.gmaaps.core.nssdk.mapper

import app.gmaaps.core.nssdk.localmodel.ApiPermission
import app.gmaaps.core.nssdk.localmodel.ApiPermissions
import app.gmaaps.core.nssdk.remotemodel.RemoteApiPermission
import app.gmaaps.core.nssdk.remotemodel.RemoteApiPermissions
import app.gmaaps.core.nssdk.remotemodel.read

internal fun RemoteApiPermissions.toLocal(): ApiPermissions =
    ApiPermissions(
        deviceStatus = deviceStatus.toLocal(),
        entries = entries.toLocal(),
        food = food.toLocal(),
        profile = profile.toLocal(),
        settings = settings.toLocal(),
        treatments = treatments.toLocal()
    )

internal fun RemoteApiPermission.toLocal(): ApiPermission =
    ApiPermission(create = create, read = read, update = update, delete = delete)

internal val RemoteApiPermission.create: Boolean
    get() = this.contains('c')

internal val RemoteApiPermission.read: Boolean
    get() = this.contains('r')

internal val RemoteApiPermission.update: Boolean
    get() = this.contains('u')

internal val RemoteApiPermission.delete: Boolean
    get() = this.contains('d')

