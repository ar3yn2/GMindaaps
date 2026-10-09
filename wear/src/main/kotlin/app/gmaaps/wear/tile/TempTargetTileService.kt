package app.gmaaps.wear.tile

import app.gmaaps.core.interfaces.di.injectMetroMembers
import app.gmaaps.wear.tile.source.TempTargetSource
import dev.zacsweers.metro.Inject

class TempTargetTileService : TileBase() {

    @Inject lateinit var tempTargetSource: TempTargetSource

    // Not derived from WearMetroService, do injection here
    override fun onCreate() {
        injectMetroMembers(this)
        super.onCreate()
    }

    override val resourceVersion = "TempTargetTileService"
    override val source get() = tempTargetSource
}

