package app.gmaaps.wear.sharedPreferences

import app.gmaaps.core.interfaces.sharedPreferences.SP
import app.gmaaps.core.interfaces.utils.SafeParse
import app.gmaaps.core.keys.BooleanKey
import app.gmaaps.core.keys.BooleanNonKey
import app.gmaaps.core.keys.DoubleKey
import app.gmaaps.core.keys.IntComposedKey
import app.gmaaps.core.keys.IntKey
import app.gmaaps.core.keys.IntNonKey
import app.gmaaps.core.keys.IntentKey
import app.gmaaps.core.keys.LongComposedKey
import app.gmaaps.core.keys.LongNonKey
import app.gmaaps.core.keys.StringKey
import app.gmaaps.core.keys.StringNonKey
import app.gmaaps.core.keys.UnitDoubleKey
import app.gmaaps.core.keys.interfaces.BooleanComposedNonPreferenceKey
import app.gmaaps.core.keys.interfaces.BooleanNonPreferenceKey
import app.gmaaps.core.keys.interfaces.BooleanPreferenceKey
import app.gmaaps.core.keys.interfaces.ComposedKey
import app.gmaaps.core.keys.interfaces.DoubleComposedNonPreferenceKey
import app.gmaaps.core.keys.interfaces.DoubleNonPreferenceKey
import app.gmaaps.core.keys.interfaces.DoublePreferenceKey
import app.gmaaps.core.keys.interfaces.IntComposedNonPreferenceKey
import app.gmaaps.core.keys.interfaces.IntNonPreferenceKey
import app.gmaaps.core.keys.interfaces.IntPreferenceKey
import app.gmaaps.core.keys.interfaces.LongComposedNonPreferenceKey
import app.gmaaps.core.keys.interfaces.LongNonPreferenceKey
import app.gmaaps.core.keys.interfaces.LongPreferenceKey
import app.gmaaps.core.keys.interfaces.NonPreferenceKey
import app.gmaaps.core.keys.interfaces.PreferenceKey
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.StringComposedNonPreferenceKey
import app.gmaaps.core.keys.interfaces.StringNonPreferenceKey
import app.gmaaps.core.keys.interfaces.StringPreferenceKey
import app.gmaaps.core.keys.interfaces.UnitDoublePreferenceKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.ConcurrentHashMap

@SingleIn(AppScope::class)
@Inject
class PreferencesImpl(
    private val sp: SP
) : Preferences {

    override val simpleMode: Boolean = false
    override val apsMode: Boolean = false
    override val nsclientMode: Boolean = false
    override val pumpControlMode: Boolean = false

    private val prefsList: MutableSet<NonPreferenceKey> =
        (BooleanKey.entries +
            BooleanNonKey.entries +
            IntKey.entries +
            IntNonKey.entries +
            IntComposedKey.entries +
            LongNonKey.entries +
            LongComposedKey.entries +
            DoubleKey.entries +
            UnitDoubleKey.entries +
            StringKey.entries +
            StringNonKey.entries +
            IntentKey.entries).toCollection(LinkedHashSet())

    /**
     * A cached flow and the read that produced its value.
     *
     * The reader is kept so [reloadFromStore] can re-read without looking the key up from the cached
     * NAME - which cannot work for a `ComposedKey`, because the name is the composed form and the
     * registry only holds the template.
     */
    private class Observed<T>(val flow: MutableStateFlow<T>, val read: () -> T)

    private fun <T> ConcurrentHashMap<String, Observed<T>>.observed(name: String, read: () -> T): StateFlow<T> =
        getOrPut(name) { Observed(MutableStateFlow(read()), read) }.flow

    private val booleanFlows = ConcurrentHashMap<String, Observed<Boolean>>()
    private val stringFlows = ConcurrentHashMap<String, Observed<String>>()
    private val doubleFlows = ConcurrentHashMap<String, Observed<Double>>()
    private val intFlows = ConcurrentHashMap<String, Observed<Int>>()
    private val longFlows = ConcurrentHashMap<String, Observed<Long>>()

    override fun get(key: BooleanNonPreferenceKey): Boolean = sp.getBoolean(key.key, key.defaultValue)

    override fun getIfExists(key: BooleanNonPreferenceKey): Boolean? =
        if (sp.contains(key.key)) sp.getBoolean(key.key, key.defaultValue) else null

    override fun put(key: BooleanNonPreferenceKey, value: Boolean) {
        sp.putBoolean(key.key, value)
        booleanFlows[key.key]?.flow?.value = value
    }

    override fun observe(key: BooleanNonPreferenceKey): StateFlow<Boolean> =
        booleanFlows.observed(key.key) { get(key) }

    override fun get(key: BooleanPreferenceKey): Boolean = sp.getBoolean(key.key, key.defaultValue)

    override fun get(key: StringNonPreferenceKey): String = sp.getString(key.key, key.defaultValue)

    override fun get(key: StringPreferenceKey): String = sp.getString(key.key, key.defaultValue)

    override fun getIfExists(key: StringNonPreferenceKey): String? =
        if (sp.contains(key.key)) sp.getString(key.key, key.defaultValue) else null

    override fun put(key: StringNonPreferenceKey, value: String) {
        sp.putString(key.key, value)
        stringFlows[key.key]?.flow?.value = value
    }

    override fun observe(key: StringNonPreferenceKey): StateFlow<String> =
        stringFlows.observed(key.key) { get(key) }

    override fun get(key: DoublePreferenceKey): Double = sp.getDouble(key.key, key.defaultValue)

    override fun get(key: DoubleNonPreferenceKey): Double = sp.getDouble(key.key, key.defaultValue)

    override fun getIfExists(key: DoublePreferenceKey): Double? =
        if (sp.contains(key.key)) sp.getDouble(key.key, key.defaultValue) else null

    override fun put(key: DoubleNonPreferenceKey, value: Double) {
        sp.putDouble(key.key, value)
        doubleFlows[key.key]?.flow?.value = value
    }

    override fun observe(key: DoubleNonPreferenceKey): StateFlow<Double> =
        doubleFlows.observed(key.key) { get(key) }

    override fun get(key: UnitDoublePreferenceKey): Double =
        error("Not implemented")
    //profileUtil.valueInCurrentUnitsDetect(sp.getDouble(key.key, key.defaultValue))

    override fun getIfExists(key: UnitDoublePreferenceKey): Double =
        error("Not implemented")
    //if (sp.contains(key.key)) sp.getDouble(key.key, key.defaultValue) else null

    override fun put(key: UnitDoublePreferenceKey, value: Double) {
        sp.putDouble(key.key, value)
    }

    override fun observe(key: UnitDoublePreferenceKey): StateFlow<Double> =
        MutableStateFlow(0.0) // Not used on wear

    override fun get(key: DoubleComposedNonPreferenceKey, vararg arguments: Any): Double =
        sp.getDouble(key.composeKey(*arguments), key.defaultValue)

    override fun getIfExists(key: DoubleComposedNonPreferenceKey, vararg arguments: Any): Double? =
        if (sp.contains(key.composeKey(*arguments))) sp.getDouble(key.composeKey(*arguments), key.defaultValue) else null

    override fun put(key: DoubleComposedNonPreferenceKey, vararg arguments: Any, value: Double) {
        sp.putDouble(key.composeKey(*arguments), value)
    }

    override fun observe(key: DoubleComposedNonPreferenceKey, vararg arguments: Any): StateFlow<Double> =
        doubleFlows.observed(key.composeKey(*arguments)) { get(key, *arguments) }

    override fun get(key: IntNonPreferenceKey): Int = sp.getInt(key.key, key.defaultValue)

    override fun getIfExists(key: IntNonPreferenceKey): Int? =
        if (sp.contains(key.key)) sp.getInt(key.key, key.defaultValue) else null

    override fun put(key: IntNonPreferenceKey, value: Int) {
        sp.putInt(key.key, value)
        intFlows[key.key]?.flow?.value = value
    }

    override fun observe(key: IntNonPreferenceKey): StateFlow<Int> =
        intFlows.observed(key.key) { get(key) }

    override fun inc(key: IntNonPreferenceKey) {
        sp.incInt(key.key)
        intFlows[key.key]?.let { it.flow.value = get(key) }
    }

    override fun get(key: IntComposedNonPreferenceKey, vararg arguments: Any): Int =
        sp.getInt(key.composeKey(*arguments), key.defaultValue)

    override fun put(key: IntComposedNonPreferenceKey, vararg arguments: Any, value: Int) {
        sp.putInt(key.composeKey(*arguments), value)
    }

    override fun observe(key: IntComposedNonPreferenceKey, vararg arguments: Any): StateFlow<Int> =
        intFlows.observed(key.composeKey(*arguments)) { get(key, *arguments) }

    override fun get(key: IntPreferenceKey): Int = sp.getInt(key.key, key.defaultValue)

    override fun get(key: LongNonPreferenceKey): Long = sp.getLong(key.key, key.defaultValue)

    override fun getIfExists(key: LongNonPreferenceKey): Long? =
        if (sp.contains(key.key)) sp.getLong(key.key, key.defaultValue) else null

    override fun put(key: LongNonPreferenceKey, value: Long) {
        sp.putLong(key.key, value)
        longFlows[key.key]?.flow?.value = value
    }

    override fun observe(key: LongNonPreferenceKey): StateFlow<Long> =
        longFlows.observed(key.key) { get(key) }

    override fun get(key: LongPreferenceKey): Long = sp.getLong(key.key, key.defaultValue)

    override fun inc(key: LongNonPreferenceKey) {
        sp.incLong(key.key)
        longFlows[key.key]?.let { it.flow.value = get(key) }
    }

    override fun remove(key: NonPreferenceKey) {
        sp.remove(key.key)
    }

    override fun get(key: LongComposedNonPreferenceKey, vararg arguments: Any): Long =
        sp.getLong(key.composeKey(*arguments), key.defaultValue)

    override fun getIfExists(key: LongComposedNonPreferenceKey, vararg arguments: Any): Long? =
        if (sp.contains(key.composeKey(*arguments))) sp.getLong(key.composeKey(*arguments), key.defaultValue) else null

    override fun put(key: LongComposedNonPreferenceKey, vararg arguments: Any, value: Long) {
        sp.putLong(key.composeKey(*arguments), value)
    }

    override fun observe(key: LongComposedNonPreferenceKey, vararg arguments: Any): StateFlow<Long> =
        longFlows.observed(key.composeKey(*arguments)) { get(key, *arguments) }

    override fun remove(key: ComposedKey, vararg arguments: Any) {
        sp.remove(key.composeKey(*arguments))
    }

    override fun isUnitDependent(key: String): Boolean =
        prefsList
            .filterIsInstance<UnitDoublePreferenceKey>()
            .any { it.key == key }

    override fun get(key: String): NonPreferenceKey? =
        prefsList
            .find { it.key == key }

    override fun getIfExists(key: String): NonPreferenceKey? =
        prefsList
            .find { it.key == key }

    override fun get(key: BooleanComposedNonPreferenceKey, vararg arguments: Any): Boolean =
        sp.getBoolean(key.composeKey(*arguments), key.defaultValue)

    override fun get(key: BooleanComposedNonPreferenceKey, vararg arguments: Any, defaultValue: Boolean): Boolean =
        sp.getBoolean(key.composeKey(*arguments), defaultValue)

    override fun getIfExists(key: BooleanComposedNonPreferenceKey, vararg arguments: Any): Boolean? =
        if (sp.contains(key.composeKey(*arguments))) sp.getBoolean(key.composeKey(*arguments), key.defaultValue) else null

    override fun put(key: BooleanComposedNonPreferenceKey, vararg arguments: Any, value: Boolean) {
        sp.putBoolean(key.composeKey(*arguments), value)
    }

    override fun observe(key: BooleanComposedNonPreferenceKey, vararg arguments: Any): StateFlow<Boolean> =
        booleanFlows.observed(key.composeKey(*arguments)) { get(key, *arguments) }

    override fun get(key: StringComposedNonPreferenceKey, vararg arguments: Any): String =
        sp.getString(key.composeKey(*arguments), key.defaultValue)

    override fun getIfExists(key: StringComposedNonPreferenceKey, vararg arguments: Any): String? =
        if (sp.contains(key.composeKey(*arguments))) sp.getString(key.composeKey(*arguments), key.defaultValue) else null

    override fun put(key: StringComposedNonPreferenceKey, vararg arguments: Any, value: String) {
        sp.putString(key.composeKey(*arguments), value)
    }

    override fun observe(key: StringComposedNonPreferenceKey, vararg arguments: Any): StateFlow<String> =
        stringFlows.observed(key.composeKey(*arguments)) { get(key, *arguments) }

    override fun registerPreferences(keys: List<NonPreferenceKey>) {
        prefsList.addAll(keys)
    }

    override fun allMatchingStrings(key: ComposedKey): List<String> =
        mutableListOf<String>().also {
            assert(key.format == "%s")
            val keys: Map<String, *> = sp.getAll()
            for ((singleKey, _) in keys)
                if (singleKey.startsWith(key.key)) it.add(singleKey.split(key.key)[1])
        }

    override fun allMatchingInts(key: ComposedKey): List<Int> =
        mutableListOf<Int>().also {
            assert(key.format == "%d")
            val keys: Map<String, *> = sp.getAll()
            for ((singleKey, _) in keys)
                if (singleKey.startsWith(key.key)) it.add(SafeParse.stringToInt(singleKey.split(key.key)[1]))
        }

    override fun isExportableKey(key: String): Boolean {
        prefsList
            .forEach {
                if (it.key == key) return true
                if (it is ComposedKey && key.startsWith(it.key)) return true
            }
        return false
    }

    override fun getAllPreferenceKeys(): List<PreferenceKey> =
        prefsList.filterIsInstance<PreferenceKey>()

    override fun getAllKeys(): List<NonPreferenceKey> = prefsList.toList()

    /**
     * Wear has no settings import, so nothing calls this today. It is implemented rather than left as
     * a no-op because wear has the same staleness hole the phone had: its
     * `OnSharedPreferenceChangeListener` sends a broadcast and an rxBus event, and never touches
     * these flows - so anything that writes wear's store without going through `put` leaves every
     * `observe(...)` stale for the life of the process. A silent no-op here would hide that.
     */
    override fun reloadFromStore() {
        booleanFlows.values.forEach { it.flow.value = it.read() }
        stringFlows.values.forEach { it.flow.value = it.read() }
        doubleFlows.values.forEach { it.flow.value = it.read() }
        intFlows.values.forEach { it.flow.value = it.read() }
        longFlows.values.forEach { it.flow.value = it.read() }
    }
}
