package app.gmaaps.pump.eopatch

import app.gmaaps.pump.eopatch.alarm.IAlarmManager
import app.gmaaps.pump.eopatch.ble.IPatchManager
import app.gmaaps.pump.eopatch.ble.PatchManagerExecutor
import app.gmaaps.pump.eopatch.ble.PreferenceManager
import app.gmaaps.pump.eopatch.vo.NormalBasalManager
import app.gmaaps.pump.eopatch.vo.PatchConfig
import app.gmaaps.shared.tests.TestBaseWithProfile
import org.mockito.Mock

open class EopatchTestBase : TestBaseWithProfile() {

    @Mock lateinit var patchManager: IPatchManager
    @Mock lateinit var patchManagerExecutor: PatchManagerExecutor
    @Mock lateinit var alarmManager: IAlarmManager
    @Mock lateinit var eopatchPreferenceManager: PreferenceManager

    lateinit var patchConfig: PatchConfig
    lateinit var normalBasalManager: NormalBasalManager

    fun prepareMocks() {
        patchConfig = PatchConfig()
        normalBasalManager = NormalBasalManager()
    }
}

