package app.gmaaps.core.interfaces.utils

import app.gmaaps.core.data.model.RM
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.data.model.TT
import app.gmaaps.core.data.ue.Action
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.data.ue.ValueWithUnit

interface Translator {

    fun translate(action: Action): String
    fun translate(units: ValueWithUnit?): String
    fun translate(meterType: TE.MeterType?): String
    fun translate(type: TE.Type?): String
    fun translate(location: TE.Location?): String
    fun translate(arrow: TE.Arrow?): String
    fun translate(reason: TT.Reason?): String
    fun translate(mode: RM.Mode?): String
    fun translate(source: Sources): String
}
