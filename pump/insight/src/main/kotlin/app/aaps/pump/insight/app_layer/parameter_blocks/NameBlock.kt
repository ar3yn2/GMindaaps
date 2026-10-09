package app.gmaaps.pump.insight.app_layer.parameter_blocks

import app.gmaaps.pump.insight.utils.ByteBuf

abstract class NameBlock : ParameterBlock() {

    lateinit var name: String

    override fun parse(byteBuf: ByteBuf) {
        name = byteBuf.readUTF16(40)
    }

    override val data: ByteBuf
        get() {
            val byteBuf = ByteBuf(42)
            byteBuf.putUTF16(name, 40)
            return byteBuf
        }
}
