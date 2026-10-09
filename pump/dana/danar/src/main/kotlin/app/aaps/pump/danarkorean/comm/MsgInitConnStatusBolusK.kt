package app.gmaaps.pump.danarkorean.comm

import app.gmaaps.core.interfaces.di.MetroMemberInjector
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.notifications.NotificationId
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.pump.danar.comm.MessageBase

class MsgInitConnStatusBolusK(
    injector: MetroMemberInjector
) : MessageBase(injector) {

    init {
        setCommand(0x0302)
        aapsLogger.debug(LTag.PUMPCOMM, "New message")
    }

    override fun handleMessage(bytes: ByteArray) {
        if (bytes.size - 10 < 13) {
            return
        }
        val bolusConfig = intFromBuff(bytes, 0, 1)
        danaPump.isExtendedBolusEnabled = bolusConfig and 0x01 != 0
        danaPump.bolusStep = intFromBuff(bytes, 1, 1) / 100.0
        danaPump.maxBolus = intFromBuff(bytes, 2, 2) / 100.0
        //int bolusRate = intFromBuff(bytes, 4, 8);
        val deliveryStatus = intFromBuff(bytes, 12, 1)
        aapsLogger.debug(LTag.PUMPCOMM, "Is Extended bolus enabled: " + danaPump.isExtendedBolusEnabled)
        aapsLogger.debug(LTag.PUMPCOMM, "Bolus increment: " + danaPump.bolusStep)
        aapsLogger.debug(LTag.PUMPCOMM, "Bolus max: " + danaPump.maxBolus)
        aapsLogger.debug(LTag.PUMPCOMM, "Delivery status: $deliveryStatus")
        if (!danaPump.isExtendedBolusEnabled) {
            notificationManager.post(NotificationId.EXTENDED_BOLUS_DISABLED, TextRef.AndroidRes(app.gmaaps.pump.dana.R.string.danar_enableextendedbolus))
        } else {
            notificationManager.dismiss(NotificationId.EXTENDED_BOLUS_DISABLED)
        }
        // This is last message of initial sequence
        activePlugin.activePump.finishHandshaking()
    }
}
