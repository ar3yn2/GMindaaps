package app.gmaaps.pump.omnipod.dash.history.database

import androidx.room.TypeConverter
import app.gmaaps.core.interfaces.profile.Profile
import app.gmaaps.pump.omnipod.common.bledriver.pod.definition.BolusType
import app.gmaaps.pump.omnipod.common.definition.OmnipodCommandType
import app.gmaaps.pump.omnipod.dash.history.data.InitialResult
import app.gmaaps.pump.omnipod.dash.history.data.ResolvedResult
import com.google.gson.GsonBuilder

class Converters {

    @TypeConverter
    fun toBolusType(s: String) = enumValueOf<BolusType>(s)

    @TypeConverter
    fun fromBolusType(bolusType: BolusType) = bolusType.name

    @TypeConverter
    fun toInitialResult(s: String) = enumValueOf<InitialResult>(s)

    @TypeConverter
    fun fromInitialResult(initialResult: InitialResult) = initialResult.name

    @TypeConverter
    fun toResolvedResult(s: String?) = s?.let { enumValueOf<ResolvedResult>(it) }

    @TypeConverter
    fun fromResolvedResult(resolvedResult: ResolvedResult?) = resolvedResult?.name

    @TypeConverter
    fun toOmnipodCommandType(s: String) = enumValueOf<OmnipodCommandType>(s)

    @TypeConverter
    fun fromOmnipodCommandType(omnipodCommandType: OmnipodCommandType) = omnipodCommandType.name

    @TypeConverter
    fun toSegments(s: String?): List<Profile.ProfileValue> {
        s ?: return emptyList()
        val gson = GsonBuilder().create()
        return gson.fromJson(s, Array<Profile.ProfileValue>::class.java).toList()
    }

    @TypeConverter
    fun fromBasalValues(segments: List<Profile.ProfileValue>): String {
        val gson = GsonBuilder().create()
        return gson.toJson(segments)
    }
}

