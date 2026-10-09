package app.gmaaps.pump.danar.comm

import app.gmaaps.core.data.plugin.PluginType
import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.notifications.NotificationId
import app.gmaaps.core.keys.interfaces.TextRef
import kotlinx.coroutines.launch

class MsgInitConnStatusTime(
    injector: MetroMemberInjector
) : MessageBase(injector) {

    init {
        setCommand(0x0301)
        aapsLogger.debug(LTag.PUMPCOMM, "New message")
    }

    override fun handleMessage(bytes: ByteArray) {
        if (bytes.size - 10 > 7) {
            notificationManager.post(NotificationId.WRONG_DRIVER, TextRef.AndroidRes(app.gmaaps.pump.dana.R.string.pumpdrivercorrected))
            danaRPlugin.disconnect("Wrong Model")
            aapsLogger.debug(LTag.PUMPCOMM, "Wrong model selected. Switching to Korean DanaR")
            danaRKoreanPlugin.setPluginEnabled(PluginType.PUMP, true)
            danaRPlugin.setPluginEnabled(PluginType.PUMP, false)
            danaPump.reset() // mark not initialized
            pumpSync.connectNewPump()
            //If profile coming from pump, switch it as well
            configBuilder.storeSettings("ChangingDanaDriver")
            // Queue-worker deadlock guard — don't unwrap the .launch. See CommandQueue kdoc.
            appScope.launch { commandQueue.readStatus(rh.gs(app.gmaaps.core.ui.R.string.pump_driver_change)) } // force new connection
            failed = false
            return
        } else {
            failed = true
        }
        val time = dateTimeSecFromBuff(bytes, 0)
        val versionCode = intFromBuff(bytes, 6, 1)
        aapsLogger.debug(LTag.PUMPCOMM, "Pump time: " + dateUtil.dateAndTimeString(time))
        aapsLogger.debug(LTag.PUMPCOMM, "Version code: $versionCode")
    }
}
