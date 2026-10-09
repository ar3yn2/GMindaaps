package app.gmaaps.wear.tile

import app.gmaaps.core.interfaces.di.injectMetroMembers
import app.gmaaps.wear.tile.source.UserActionSource
import dev.zacsweers.metro.Inject

class UserActionTileService : TileBase() {

    @Inject lateinit var userActionSource: UserActionSource

    // Not derived from WearMetroService, do injection here
    override fun onCreate() {
        injectMetroMembers(this)
        super.onCreate()
    }

    override val resourceVersion = "UserActionTileService"
    override val source get() = userActionSource
}

