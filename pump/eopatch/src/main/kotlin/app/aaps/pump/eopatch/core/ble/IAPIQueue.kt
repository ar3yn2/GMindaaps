package app.gmaaps.pump.eopatch.core.ble

import io.reactivex.rxjava3.core.Observable

interface IAPIQueue {
    fun getTurn(function: PatchFunc): Observable<PatchFunc>
}

