package app.gmaaps.pump.medtrum.compose.steps

internal fun String.stripHtml(): String = this.replace(Regex("<[^>]*>"), "")

