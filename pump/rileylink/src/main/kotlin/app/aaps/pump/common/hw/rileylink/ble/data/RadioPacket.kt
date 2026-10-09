package app.gmaaps.pump.common.hw.rileylink.ble.data

import app.gmaaps.core.utils.pump.ByteUtil.concat
import app.gmaaps.pump.common.hw.rileylink.RileyLinkUtil
import app.gmaaps.pump.common.hw.rileylink.ble.defs.RileyLinkEncodingType
import app.gmaaps.pump.common.utils.CRC
import org.apache.commons.lang3.NotImplementedException

/**
 * Created by geoff on 5/22/16.
 */
class RadioPacket(private val rileyLinkUtil: RileyLinkUtil, val pkt: ByteArray) {

    private fun getWithCRC(): ByteArray = concat(pkt, CRC.crc8(pkt))

    fun getEncoded(): ByteArray {
        when (rileyLinkUtil.encoding) {
            RileyLinkEncodingType.Manchester               -> {
                // We have this encoding in RL firmware
                return pkt
            }

            RileyLinkEncodingType.FourByteSixByteLocal     -> {
                val withCRC = getWithCRC()

                val encoded = rileyLinkUtil.encoding4b6b.encode4b6b(withCRC)
                return concat(encoded, 0.toByte())
            }

            RileyLinkEncodingType.FourByteSixByteRileyLink -> {
                return getWithCRC()
            }

            else                                           -> throw NotImplementedException(("Encoding not supported: " + rileyLinkUtil.encoding.toString()))
        }
    }
}

