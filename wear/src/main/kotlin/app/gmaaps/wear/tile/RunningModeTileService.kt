package app.gmaaps.wear.tile

import androidx.wear.tiles.EventBuilders.TileEnterEvent
import app.gmaaps.core.interfaces.di.injectMetroMembers
import app.gmaaps.core.interfaces.rx.bus.RxBus
import app.gmaaps.core.interfaces.rx.events.EventWearToMobile
import app.gmaaps.core.interfaces.rx.weardata.EventData
import app.gmaaps.wear.tile.source.RunningModeSource
import dev.zacsweers.metro.Inject

class RunningModeTileService : TileBase() {

    @Inject lateinit var runningModeSource: RunningModeSource
    @Inject lateinit var rxBus: RxBus

    // Not derived from WearMetroService, do injection here
    override fun onCreate() {
        injectMetroMembers(this)
        super.onCreate()
    }

    override val resourceVersion = "RunningModeTileService"
    override val source get() = runningModeSource

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onTileEnterEvent(requestParams: TileEnterEvent) {
        rxBus.send(EventWearToMobile(EventData.RunningModeRequest(System.currentTimeMillis())))
    }
}



