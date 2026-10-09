package app.gmaaps.pump.insight.app_layer.parameter_blocks

import app.gmaaps.pump.insight.utils.ByteBuf

abstract class ParameterBlock {

    abstract fun parse(byteBuf: ByteBuf)
    abstract val data: ByteBuf?
}
