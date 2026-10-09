package app.gmaaps.core.nssdk.mapper

import app.gmaaps.core.nssdk.localmodel.treatment.NSICfg
import app.gmaaps.core.nssdk.remotemodel.RemoteICfg

internal fun RemoteICfg?.toNSICfg(): NSICfg? {

    this ?: return null

    return NSICfg(
        insulinLabel = insulinLabel,
        insulinEndTime = insulinEndTime,
        insulinPeakTime = insulinPeakTime,
        concentration = concentration
    )
}

internal fun NSICfg?.toRemoteICfg(): RemoteICfg? {

    this ?: return null

    return RemoteICfg(
        insulinLabel = insulinLabel,
        insulinEndTime = insulinEndTime,
        insulinPeakTime = insulinPeakTime,
        concentration = concentration
    )
}
