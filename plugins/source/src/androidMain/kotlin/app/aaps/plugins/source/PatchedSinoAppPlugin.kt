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
import app.gmaaps.core.ui.compose.icons.IcGenericCgm
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
import org.json.JSONArray
import org.json.JSONException

@ContributesIntoMap(AppScope::class, binding = binding<PluginBase>())
@IntKey(520)
@SingleIn(AppScope::class)
@Inject
class PatchedSinoAppPlugin(
    rh: ResourceHelper,
    aapsLogger: AAPSLogger,
    preferences: Preferences,
    config: Config,
    notificationManager: NotificationManager
) : AbstractBgSourcePlugin(
    PluginDescription()
        .mainType(PluginType.BGSOURCE)
        .composeContent { plugin ->
            BgSourceComposeContent(
                title = rh.gs(R.string.patched_sino_app)
            )
        }
        .icon(IcGenericCgm)
        .pluginName(TextRef.AndroidRes(R.string.patched_sino_app))
        .preferencesVisibleInSimpleMode(false)
        .description(TextRef.AndroidRes(R.string.description_source_patched_sino_app)),
    ownPreferences = emptyList(),
    aapsLogger, rh, preferences, config, notificationManager
), BgSource {


    @AssistedInject
    class PatchedSinoAppWorker(
        @Assisted context: Context,
        @Assisted params: WorkerParameters,
        aapsLogger: AAPSLogger,
        fabricPrivacy: FabricPrivacy,
        private val patchedSinoAppPlugin: PatchedSinoAppPlugin,
        private val persistenceLayer: PersistenceLayer
    ) : LoggingWorker(context, params, Dispatchers.IO, aapsLogger, fabricPrivacy) {

        /**
         * Metro builds this worker. The parameter names must match [MetroWorkerCreator],
         * because Metro matches assisted parameters by name and not only by type.
         */
        @AssistedFactory
        fun interface Factory : MetroWorkerCreator {

            override fun create(context: Context, params: WorkerParameters): PatchedSinoAppWorker
        }

        @SuppressLint("CheckResult")
        override suspend fun doWorkAndLog(): Result {
            var ret = Result.success()
            if (!patchedSinoAppPlugin.isEnabled()) return Result.success(workDataOf("Result" to "Plugin not enabled"))
            val collection = inputData.getString("collection") ?: return Result.failure(workDataOf("Error" to "missing collection"))
            if (collection == "entries") {
                val data = inputData.getString("data")
                aapsLogger.debug(LTag.BGSOURCE, "Received Sino App Data $data")
                if (!data.isNullOrEmpty()) {
                    try {
                        val glucoseValues = mutableListOf<GV>()
                        val jsonArray = JSONArray(data)
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            when (val type = jsonObject.getString("type")) {
                                "sgv" ->
                                    glucoseValues += GV(
                                        timestamp = jsonObject.getLong("date"),
                                        value = jsonObject.getDouble("sgv"),
                                        raw = null,
                                        noise = null,
                                        trendArrow = TrendArrow.fromString(jsonObject.getString("direction")),
                                        sourceSensor = SourceSensor.SINOCARE
                                    )

                                else  -> aapsLogger.debug(LTag.BGSOURCE, "Unknown entries type: $type")
                            }
                        }
                        try {
                            persistenceLayer.insertCgmSourceData(Sources.Sino, glucoseValues, emptyList(), null)
                        } catch (e: Exception) {
                            ret = Result.failure(workDataOf("Error" to e.toString()))
                        }
                    } catch (e: JSONException) {
                        aapsLogger.error("Exception: ", e)
                        ret = Result.failure(workDataOf("Error" to e.toString()))
                    }
                }
            } else {
                ret = Result.failure(workDataOf("Error" to "missing input data"))
            }
            return ret
        }
    }
}

