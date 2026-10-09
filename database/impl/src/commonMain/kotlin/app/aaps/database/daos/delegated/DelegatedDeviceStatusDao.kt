package app.gmaaps.database.daos.delegated

import app.gmaaps.database.daos.DeviceStatusDao
import app.gmaaps.database.entities.interfaces.DBEntry

internal class DelegatedDeviceStatusDao(changes: MutableList<DBEntry>, private val dao: DeviceStatusDao) : DelegatedDao(changes), DeviceStatusDao by dao
