package app.gmaaps.pump.danarkorean.comm

import app.gmaaps.core.data.plugin.PluginType
import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.notifications.NotificationId
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.pump.danar.comm.MessageBase
import kotlinx.coroutines.launch

class MsgInitConnStatusTimeK(
    injector: MetroMemberInjector
) : MessageBase(injector) {

    init {
        setCommand(0x0301)
        aapsLogger.debug(LTag.PUMPCOMM, "New message")
    }

    override fun handleMessage(bytes: ByteArray) {
        if (bytes.size - 10 < 10) {
            notificationManager.post(NotificationId.WRONG_DRIVER, TextRef.AndroidRes(app.gmaaps.pump.dana.R.string.pumpdrivercorrected))
            danaRKoreanPlugin.disconnect("Wrong Model")
            aapsLogger.debug(LTag.PUMPCOMM, "Wrong model selected. Switching to export DanaR")
            danaRKoreanPlugin.setPluginEnabled(PluginType.PUMP, false)
            danaRPlugin.setPluginEnabled(PluginType.PUMP, true)
            danaPump.reset() // mark not initialized
            pumpSync.connectNewPump()
            //If profile coming from pump, switch it as well
            configBuilder.storeSettings("ChangingKoreanDanaDriver")
            // Queue-worker deadlock guard — don't unwrap the .launch. See CommandQueue kdoc.
            appScope.launch { commandQueue.readStatus(rh.gs(app.gmaaps.core.ui.R.string.pump_driver_change)) } // force new connection
            return
        }
        val time = dateTimeSecFromBuff(bytes, 0)
        val versionCode1 = intFromBuff(bytes, 6, 1)
        val versionCode2 = intFromBuff(bytes, 7, 1)
        val versionCode3 = intFromBuff(bytes, 8, 1)
        val versionCode4 = intFromBuff(bytes, 9, 1)
        aapsLogger.debug(LTag.PUMPCOMM, "Pump time: " + dateUtil.dateAndTimeString(time))
        aapsLogger.debug(LTag.PUMPCOMM, "Version code1: $versionCode1")
        aapsLogger.debug(LTag.PUMPCOMM, "Version code2: $versionCode2")
        aapsLogger.debug(LTag.PUMPCOMM, "Version code3: $versionCode3")
        aapsLogger.debug(LTag.PUMPCOMM, "Version code4: $versionCode4")
    }
}
