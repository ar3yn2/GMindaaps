package app.gmaaps.pump.eopatch.vo

import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.pump.eopatch.CommonUtils
import app.gmaaps.pump.eopatch.GsonHelper
import app.gmaaps.pump.eopatch.code.UnitOrPercent
import app.gmaaps.pump.eopatch.keys.EopatchStringNonKey
import com.google.common.base.Preconditions
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class TempBasalManager : IPreference<TempBasalManager> {

    @Transient
    private val subject: BehaviorSubject<TempBasalManager> = BehaviorSubject.create()

    var startedBasal: TempBasal? = null

    private var startTimestamp = 0L

    private var endTimestamp = 0L

    var unit = UnitOrPercent.P

    fun clear() {
        startedBasal = null
        startTimestamp = 0L
        endTimestamp = 0L
    }

    fun updateBasalRunning(tempBasal: TempBasal) {
        Preconditions.checkNotNull(tempBasal)

        this.startedBasal = CommonUtils.clone(tempBasal)
        this.startedBasal?.running = true
        this.startTimestamp = System.currentTimeMillis()
    }

    fun updateBasalStopped() {
        this.startedBasal?.running = false
        this.startedBasal?.startTimestamp = 0
    }

    fun update(other: TempBasalManager) {
        this.startedBasal = other.startedBasal
        startTimestamp = other.startTimestamp
        endTimestamp = other.endTimestamp
        unit = other.unit
    }

    override fun observe(): Observable<TempBasalManager> {
        return subject.hide()
    }

    override fun flush(preferences: Preferences) {
        val jsonStr = GsonHelper.sharedGson().toJson(this)
        preferences.put(EopatchStringNonKey.TempBasal, jsonStr)
        subject.onNext(this)
    }

    override fun toString(): String {
        return "TempBasalManager(startedBasal=$startedBasal, startTimestamp=$startTimestamp, endTimestamp=$endTimestamp, unit=$unit)"
    }
}

