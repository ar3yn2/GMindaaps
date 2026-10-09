package app.gmaaps.wear.di

import android.content.Context
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.L
import app.gmaaps.core.interfaces.rx.AapsSchedulers
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.sharedPreferences.SP
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.shared.impl.logging.LImpl
import app.gmaaps.shared.impl.rx.AapsSchedulersImpl
import app.gmaaps.shared.impl.rx.bus.RxBusImpl
import app.gmaaps.shared.impl.sharedPreferences.SPImpl
import app.gmaaps.shared.impl.sharedPreferences.defaultPreferences
import app.gmaaps.shared.impl.utils.DateUtilImpl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

/**
 * The shared-implementation wiring for **:wear only**.
 * The phone builds all five of these in Metro too, in its own `SharedImplBindings` - the classes are
 * shared, the wiring is not. Wear has its own graph, so it needs its own copy of these bindings.
 */
@ContributesTo(AppScope::class)
@BindingContainer
object SharedImplModule {

    @Provides
    @SingleIn(AppScope::class)
    fun provideSP(context: Context): SP =
        SPImpl(defaultPreferences(context), context)

    @Provides
    @SingleIn(AppScope::class)
    fun provideL(preferences: () -> Preferences): L = LImpl { preferences() }

    @Provides
    @SingleIn(AppScope::class)
    fun provideDateUtil(context: Context): DateUtil = DateUtilImpl(context)

    @Provides
    @SingleIn(AppScope::class)
    fun provideRxBus(aapsLogger: AAPSLogger): RxBus = RxBusImpl(aapsLogger)

    @Provides
    @SingleIn(AppScope::class)
    internal fun provideSchedulers(): AapsSchedulers = AapsSchedulersImpl()
}

