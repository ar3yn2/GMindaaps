package app.gmaaps.wear.tile

import app.gmaaps.core.interfaces.di.injectMetroMembers
import app.gmaaps.wear.tile.source.ActionSource
import dev.zacsweers.metro.Inject

class ActionsTileService : TileBase() {

    @Inject lateinit var actionSource: ActionSource

    // Not derived from WearMetroService, do injection here
    override fun onCreate() {
        injectMetroMembers(this)
        super.onCreate()
    }

    override val resourceVersion = "ActionsTileService"
    override val source get() = actionSource
}

