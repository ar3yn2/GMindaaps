package app.gmaaps.di.metro

import app.gmaaps.plugins.source.AidexPlugin
import app.gmaaps.plugins.source.DexcomPlugin
import app.gmaaps.plugins.source.GlimpPlugin
import app.gmaaps.plugins.source.MM640gPlugin
import app.gmaaps.plugins.source.PatchedSiAppPlugin
import app.gmaaps.plugins.source.PatchedSinoAppPlugin
import app.gmaaps.plugins.source.PoctechPlugin
import app.gmaaps.plugins.source.SyaiPlugin
import app.gmaaps.plugins.source.TomatoPlugin
import app.gmaaps.plugins.source.XdripSourcePlugin
import app.gmaaps.plugins.source.di.SourceMetroGraph
import app.gmaaps.plugins.source.instara.InstaraPlugin
import app.gmaaps.plugins.source.instara.InstaraStaleCheckWorker
import app.gmaaps.plugins.source.notificationreader.NotificationCollectorService
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

/**
 * Every blood-glucose source worker must be registered, under its own class.
 * Nothing is dereferenced here on purpose. Reading the map only builds the assisted factories, not the
 * workers, so no leaf has to be stubbed at all - see [testRoot].
 * The graph is opened from the root rather than built directly, because it is a `@GraphExtension` and
 * that is the only way to reach one.
 */
class SourceGraphTest {

    private fun graph(): SourceMetroGraph = testRoot().sourceGraph

    @Test
    fun `all twelve source workers are registered`() {
        assertThat(graph().workerCreators.keys).containsExactly(
            AidexPlugin.AidexWorker::class,
            DexcomPlugin.DexcomWorker::class,
            GlimpPlugin.GlimpWorker::class,
            InstaraPlugin.InstaraWorker::class,
            InstaraStaleCheckWorker::class,
            MM640gPlugin.MM640gWorker::class,
            PatchedSiAppPlugin.PatchedSiAppWorker::class,
            PatchedSinoAppPlugin.PatchedSinoAppWorker::class,
            PoctechPlugin.PoctechWorker::class,
            SyaiPlugin.SyaiWorker::class,
            TomatoPlugin.TomatoWorker::class,
            XdripSourcePlugin.XdripSourceWorker::class
        )
    }

    @Test
    fun `the notification reader service is the only member injection here`() {
        assertThat(graph().memberInjectors.keys).containsExactly(NotificationCollectorService::class)
    }
}

