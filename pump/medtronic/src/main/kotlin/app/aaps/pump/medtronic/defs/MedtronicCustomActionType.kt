package app.gmaaps.pump.medtronic.defs

import app.gmaaps.core.interfaces.pump.actions.CustomActionType

/**
 * Created by andy on 11/3/18.
 */
enum class MedtronicCustomActionType : CustomActionType {

    WakeUpAndTune,  //
    ClearBolusBlock,  //
    ResetRileyLinkConfiguration;

    //

    override fun getKey(): String {
        return name
    }
}
