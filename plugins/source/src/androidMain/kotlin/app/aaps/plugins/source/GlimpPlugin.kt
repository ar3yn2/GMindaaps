package app.gmaaps.plugins.source

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import app.gmaaps.core.data.model.GV
import app.gmaaps.core.data.model.SourceSensor
import app.gmaaps.core.data.model.TrendArrow
import app.gmaaps.core.data.plugin.PluginType
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.interfaces.configuration.Config
import app.gmaaps.core.interfaces.db.PersistenceLayer
import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.logging.LTag
import app.gmaaps.core.interfaces.notifications.NotificationManager
import app.gmaaps.core.interfaces.plugin.PluginBase
import app.gmaaps.core.interfaces.plugin.PluginDescription
import app.gmaaps.core.interfaces.resources.ResourceHelper
import app.gmaaps.core.interfaces.source.BgSource
import app.gmaaps.core.interfaces.utils.fabric.FabricPrivacy
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.objects.workflow.LoggingWorker
import app.gmaaps.core.objects.workflow.MetroWorkerCreator
import app.gmaaps.core.ui.compose.icons.IcPluginGlimp
import app.gmaaps.plugins.source.compose.BgSourceComposeContent
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.AssistedInject
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.IntKey
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.binding
import kotlinx.coroutines.Dispatchers

@ContributesIntoMap(AppScope::class, binding = binding<PluginBase>())
@IntKey(430)
@SingleIn(AppScope::class)
@Inject
class GlimpPlugin(
    rh: ResourceHelper,
    aapsLogger: AAPSLogger,
    preferences: Preferences,
    config: Config,
    notificationManager: NotificationManager
) : AbstractBgSourcePlugin(
    pluginDescription = PluginDescription()
        .mainType(PluginType.BGSOURCE)
        .composeContent { plugin ->
            BgSourceComposeContent(
                title = rh.gs(R.string.glimp)
            )
        }
        .icon(IcPluginGlimp)
        .pluginName(TextRef.AndroidRes(R.string.glimp))
        .preferencesVisibleInSimpleMode(false)
        .description(TextRef.AndroidRes(R.string.description_source_glimp)),
    ownPreferences = emptyList(),
    aapsLogger, rh, preferences, config, notificationManager
), BgSource {

    // cannot be inner class because of needed injection

    @AssistedInject
    class GlimpWorker(
        @Assisted context: Context,
        @Assisted params: WorkerParameters,
        aapsLogger: AAPSLogger,
        fabricPrivacy: FabricPrivacy,
        private val glimpPlugin: GlimpPlugin,
        private val persistenceLayer: PersistenceLayer
    ) : LoggingWorker(context, params, Dispatchers.IO, aapsLogger, fabricPrivacy) {

        /**
         * Metro builds this worker. The parameter names must match [MetroWorkerCreator],
         * because Metro matches assisted parameters by name and not only by type.
         */
        @AssistedFactory
        fun interface Factory : MetroWorkerCreator {

            override fun create(context: Context, params: WorkerParameters): GlimpWorker
        }

        @SuppressLint("CheckResult")
        override suspend fun doWorkAndLog(): Result {
            var ret = Result.success()

            if (!glimpPlugin.isEnabled()) return Result.success(workDataOf("Result" to "Plugin not enabled"))
            aapsLogger.debug(LTag.BGSOURCE, "Received Glimp Data: $inputData}")
            val glucoseValues = mutableListOf<GV>()
            glucoseValues += GV(
                timestamp = inputData.getLong("myTimestamp", 0),
                value = inputData.getDouble("mySGV", 0.0),
                raw = null,
                noise = null,
                trendArrow = TrendArrow.fromString(inputData.getString("myTrend")),
                sourceSensor = SourceSensor.LIBRE_1
            )
            try {
                persistenceLayer.insertCgmSourceData(Sources.Glimp, glucoseValues, emptyList(), null)
            } catch (e: Exception) {
                ret = Result.failure(workDataOf("Error" to e.toString()))
            }
            return ret
        }
    }
}

