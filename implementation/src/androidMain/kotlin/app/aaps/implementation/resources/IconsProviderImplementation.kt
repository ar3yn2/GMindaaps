package app.gmaaps.implementation.resources

import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.ui.IconsProvider
import app.gmaaps.implementation.R
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
@Inject
class IconsProviderImplementation(private val config: Config) : IconsProvider {

    override fun getIcon(): Int =
        when {
            config.AAPSCLIENT3 -> app.gmaaps.core.ui.R.mipmap.ic_greenowl
            config.AAPSCLIENT2 -> app.gmaaps.core.ui.R.mipmap.ic_blueowl
            config.AAPSCLIENT1 -> app.gmaaps.core.ui.R.mipmap.ic_yellowowl
            config.PUMPCONTROL -> app.gmaaps.core.ui.R.mipmap.ic_pumpcontrol
            else               -> app.gmaaps.core.ui.R.mipmap.ic_launcher
        }

    override fun getNotificationIcon(): Int =
        when {
            config.AAPSCLIENT  -> R.drawable.ic_notif_nsclient
            config.PUMPCONTROL -> R.drawable.ic_notif_pumpcontrol
            else               -> app.gmaaps.core.ui.R.drawable.ic_notif_aaps
        }
}

