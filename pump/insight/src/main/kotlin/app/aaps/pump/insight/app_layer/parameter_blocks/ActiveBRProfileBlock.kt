package app.gmaaps.pump.insight.app_layer.parameter_blocks

import app.gmaaps.pump.insight.descriptors.BasalProfile
import app.gmaaps.pump.insight.descriptors.BasalProfile.Companion.fromId
import app.gmaaps.pump.insight.utils.ByteBuf

class ActiveBRProfileBlock : ParameterBlock() {

    internal var activeBasalProfile: BasalProfile? = null
    override fun parse(byteBuf: ByteBuf) {
        activeBasalProfile = fromId(byteBuf.readUInt16LE())
    }

    override val data: ByteBuf
        get() {
            val byteBuf = ByteBuf(2)
            activeBasalProfile?.let { byteBuf.putUInt16LE(it.id) }
            return byteBuf
        }
}
