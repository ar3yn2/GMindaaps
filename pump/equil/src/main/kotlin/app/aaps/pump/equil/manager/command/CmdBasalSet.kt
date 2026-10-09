package app.gmaaps.pump.equil.manager.command

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.profile.Profile
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.utils.notifyAll
import app.gmaaps.pump.equil.database.EquilHistoryRecord
import app.gmaaps.pump.equil.driver.definition.BasalSchedule
import app.gmaaps.pump.equil.keys.EquilBooleanKey
import app.gmaaps.pump.equil.manager.EquilManager
import app.gmaaps.pump.equil.manager.Utils

class CmdBasalSet(
    var basalSchedule: BasalSchedule,
    var profile: Profile,
    aapsLogger: AAPSLogger,
    preferences: Preferences,
    equilManager: EquilManager
) : BaseSetting(System.currentTimeMillis(), aapsLogger, preferences, equilManager) {

    override fun getFirstData(): ByteArray {
        val indexByte = Utils.intToBytes(pumpReqIndex)
        val data2 = byteArrayOf(0x01, 0x02)
        val list: MutableList<Byte?> = ArrayList()
        var i = 0
        for (basalScheduleEntry in basalSchedule.getEntries()) {
            val rate = basalScheduleEntry.rate
            val value = rate / 2f
            val bs = Utils.basalToByteArray(value)
            aapsLogger.debug(
                LTag.PUMPCOMM,
                (i.toString() + "==CmdBasalSet==" + value + "====" + rate + "===" + Utils.decodeSpeedToUH(value) + "==="
                    + Utils.decodeSpeedToUHT(value))
            )
            list.add(bs[1])
            list.add(bs[0])
            list.add(bs[1])
            list.add(bs[0])
            i++
        }
        val hex = Utils.bytesToHex(list)
        val data = Utils.concat(indexByte, data2, Utils.hexStringToBytes(hex))
        pumpReqIndex++
        return data
    }

    override fun getNextData(): ByteArray {
        val indexByte = Utils.intToBytes(pumpReqIndex)
        val data2 = byteArrayOf(0x00, 0x02, 0x01)
        val data = Utils.concat(indexByte, data2)
        pumpReqIndex++
        return data
    }

    override fun decodeConfirmData(data: ByteArray) {
        synchronized(this) {
            preferences.put(EquilBooleanKey.BasalSet, true)
            cmdSuccess = true
            notifyAll()
        }
    }

    override fun getEventType(): EquilHistoryRecord.EventType {
        return EquilHistoryRecord.EventType.SET_BASAL_PROFILE
    }
}

