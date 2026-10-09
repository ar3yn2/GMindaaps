package app.gmaaps.plugins.sync.openhumans

import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.plugins.sync.openhumans.delegates.OHAppIDDelegate
import app.gmaaps.plugins.sync.openhumans.delegates.OHCounterDelegate
import app.gmaaps.plugins.sync.openhumans.delegates.OHStateDelegate
import app.gmaaps.shared.tests.TestBaseWithProfile
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock

class OpenHumansUploaderPluginTest : TestBaseWithProfile() {

    @Mock lateinit var persistenceLayer: PersistenceLayer
    @Mock lateinit var openHumansAPI: OpenHumansAPI

    private lateinit var openHumansUploaderPlugin: OpenHumansUploaderPlugin
    private lateinit var stateDelegate: OHStateDelegate
    private lateinit var counterDelegate: OHCounterDelegate
    private lateinit var appIdDelegate: OHAppIDDelegate

    @BeforeEach fun prepare() {
        stateDelegate = OHStateDelegate(preferences)
        counterDelegate = OHCounterDelegate(preferences)
        appIdDelegate = OHAppIDDelegate(preferences)
        openHumansUploaderPlugin = OpenHumansUploaderPlugin(rh, aapsLogger, preferences, context, persistenceLayer, openHumansAPI, notificationManager, stateDelegate, counterDelegate, appIdDelegate)
    }
}

