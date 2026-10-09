package app.gmaaps.wear.tile

import androidx.wear.tiles.EventBuilders.TileEnterEvent
import app.gmaaps.core.interfaces.di.injectMetroMembers
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.rx.events.EventWearToMobile
import app.gmaaps.core.interfaces.rx.weardata.EventData
import app.gmaaps.wear.tile.source.QuickWizardSource
import dev.zacsweers.metro.Inject

class QuickWizardTileService : TileBase() {

    @Inject lateinit var quickWizardSource: QuickWizardSource
    @Inject lateinit var rxBus: RxBus

    // Not derived from WearMetroService, do injection here
    override fun onCreate() {
        injectMetroMembers(this)
        super.onCreate()
    }

    override val resourceVersion = "QuickWizardTileService"
    override val source get() = quickWizardSource

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onTileEnterEvent(requestParams: TileEnterEvent) {
        rxBus.send(EventWearToMobile(EventData.ActionResendData("QuickWizardTileService")))
    }
}

