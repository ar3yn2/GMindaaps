package app.gmaaps.database.persistence.converters

import app.gmaaps.core.interfaces.aps.APSResult
import app.gmaaps.core.interfaces.aps.AutosensResult
import app.gmaaps.core.interfaces.aps.CurrentTemp
import app.gmaaps.core.interfaces.aps.GlucoseStatusAutoIsf
import app.gmaaps.core.interfaces.aps.GlucoseStatusSMB
import app.gmaaps.core.interfaces.aps.IobTotal
import app.gmaaps.core.interfaces.aps.MealData
import app.gmaaps.core.interfaces.aps.OapsProfile
import app.gmaaps.core.interfaces.aps.OapsProfileAutoIsf
import app.gmaaps.core.interfaces.aps.RT
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
private val rtJson = Json {
    allowSpecialFloatingPointValues = true
    ignoreUnknownKeys = true
}

fun app.gmaaps.database.entities.APSResult.fromDb(apsResultProvider: () -> APSResult): APSResult =
    when (algorithm) {
        app.gmaaps.database.entities.APSResult.Algorithm.AMA,
        app.gmaaps.database.entities.APSResult.Algorithm.SMB      ->
            apsResultProvider().with(rtJson.decodeFromString(this.resultJson)).also { result ->
                result.date = this.timestamp
                result.glucoseStatus = try {
                    this.glucoseStatusJson?.let { Json.decodeFromString(it) }
                } catch (_: Exception) {
                    null
                }
                result.currentTemp = this.currentTempJson?.let { Json.decodeFromString(it) }
                result.iobData = this.iobDataJson?.let { Json.decodeFromString(it) }
                result.oapsProfile = this.profileJson?.let { Json.decodeFromString(it) }
                result.mealData = this.mealDataJson?.let { Json.decodeFromString(it) }
                result.autosensResult = this.autosensDataJson?.let { Json.decodeFromString(it) }
            }

        app.gmaaps.database.entities.APSResult.Algorithm.AUTO_ISF ->
            apsResultProvider().with(rtJson.decodeFromString(this.resultJson)).also { result ->
                result.date = this.timestamp
                result.glucoseStatus = try {
                    this.glucoseStatusJson?.let { Json.decodeFromString(it) }
                } catch (_: Exception) {
                    null
                }
                result.currentTemp = this.currentTempJson?.let { Json.decodeFromString(it) }
                result.iobData = this.iobDataJson?.let { Json.decodeFromString(it) }
                result.oapsProfileAutoIsf = this.profileJson?.let { Json.decodeFromString(it) }
                result.mealData = this.mealDataJson?.let { Json.decodeFromString(it) }
                result.autosensResult = this.autosensDataJson?.let { Json.decodeFromString(it) }
            }

        else                                                    -> error("Unsupported")
    }

@OptIn(ExperimentalSerializationApi::class)
fun APSResult.toDb(): app.gmaaps.database.entities.APSResult =
    when (algorithm) {
        APSResult.Algorithm.AMA,
        APSResult.Algorithm.SMB      ->
            app.gmaaps.database.entities.APSResult(
                timestamp = this.date,
                algorithm = this.algorithm.toDb(),
                glucoseStatusJson = this.glucoseStatus?.let { Json.encodeToString(GlucoseStatusSMB.serializer(), it as GlucoseStatusSMB) },
                currentTempJson = this.currentTemp?.let { Json.encodeToString(CurrentTemp.serializer(), it) },
                iobDataJson = this.iobData?.let { Json.encodeToString(ArraySerializer(IobTotal.serializer()), it) },
                profileJson = this.oapsProfile?.let { Json.encodeToString(OapsProfile.serializer(), it) },
                mealDataJson = this.mealData?.let { Json.encodeToString(MealData.serializer(), it) },
                autosensDataJson = this.autosensResult?.let { Json.encodeToString(AutosensResult.serializer(), it) },
                resultJson = rtJson.encodeToString(RT.serializer(), this.rawData() as RT)
            )

        APSResult.Algorithm.AUTO_ISF ->
            app.gmaaps.database.entities.APSResult(
                timestamp = this.date,
                algorithm = this.algorithm.toDb(),
                glucoseStatusJson = this.glucoseStatus?.let { Json.encodeToString(GlucoseStatusAutoIsf.serializer(), it as GlucoseStatusAutoIsf) },
                currentTempJson = this.currentTemp?.let { Json.encodeToString(CurrentTemp.serializer(), it) },
                iobDataJson = this.iobData?.let { Json.encodeToString(ArraySerializer(IobTotal.serializer()), it) },
                profileJson = this.oapsProfileAutoIsf?.let { Json.encodeToString(OapsProfileAutoIsf.serializer(), it) },
                mealDataJson = this.mealData?.let { Json.encodeToString(MealData.serializer(), it) },
                autosensDataJson = this.autosensResult?.let { Json.encodeToString(AutosensResult.serializer(), it) },
                resultJson = rtJson.encodeToString(RT.serializer(), this.rawData() as RT)
            )

        else                         -> error("Unsupported")
    }

fun app.gmaaps.database.entities.APSResult.Algorithm.fromDb(): APSResult.Algorithm =
    when (this) {
        app.gmaaps.database.entities.APSResult.Algorithm.AMA      -> APSResult.Algorithm.AMA
        app.gmaaps.database.entities.APSResult.Algorithm.SMB      -> APSResult.Algorithm.SMB
        app.gmaaps.database.entities.APSResult.Algorithm.AUTO_ISF -> APSResult.Algorithm.AUTO_ISF
        else                                                    -> error("Unsupported")
    }

fun APSResult.Algorithm.toDb(): app.gmaaps.database.entities.APSResult.Algorithm =
    when (this) {
        APSResult.Algorithm.AMA      -> app.gmaaps.database.entities.APSResult.Algorithm.AMA
        APSResult.Algorithm.SMB      -> app.gmaaps.database.entities.APSResult.Algorithm.SMB
        APSResult.Algorithm.AUTO_ISF -> app.gmaaps.database.entities.APSResult.Algorithm.AUTO_ISF
        else                         -> error("Unsupported")
    }
