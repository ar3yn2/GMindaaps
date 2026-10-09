package app.gmaaps.pump.eopatch.vo

import app.gmaaps.core.keys.interfaces.Preferences
import io.reactivex.rxjava3.core.Observable

interface IPreference<T : Any> {

    fun flush(preferences: Preferences)
    fun observe(): Observable<T>
}
