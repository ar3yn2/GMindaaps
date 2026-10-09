package app.gmaaps.core.interfaces.versionChecker

import kotlinx.serialization.json.JsonObject

fun interface VersionDefinition {

    fun invoke(): JsonObject
}
