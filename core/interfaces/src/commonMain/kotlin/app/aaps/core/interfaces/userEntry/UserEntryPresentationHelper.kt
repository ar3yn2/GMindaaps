package app.gmaaps.core.interfaces.userEntry

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import app.gmaaps.core.data.model.UE
import app.gmaaps.core.data.ue.Sources
import app.gmaaps.core.data.ue.ValueWithUnit

interface UserEntryPresentationHelper {

    fun icon(source: Sources): ImageVector
    @Composable fun iconColor(source: Sources): Color
    fun listToPresentationString(list: List<ValueWithUnit>): String
    fun userEntriesToCsv(userEntries: List<UE>): String
}

