package app.gmaaps.core.ui.compose.siteRotation

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.ui.compose.icons.library.ChildBack
import app.gmaaps.core.ui.compose.icons.library.ChildBackPaths
import app.gmaaps.core.ui.compose.icons.library.ChildFront
import app.gmaaps.core.ui.compose.icons.library.ChildFrontPaths
import app.gmaaps.core.ui.compose.icons.library.ManBack
import app.gmaaps.core.ui.compose.icons.library.ManBackPaths
import app.gmaaps.core.ui.compose.icons.library.ManFront
import app.gmaaps.core.ui.compose.icons.library.ManFrontPaths
import app.gmaaps.core.ui.compose.icons.library.WomanBack
import app.gmaaps.core.ui.compose.icons.library.WomanBackPaths
import app.gmaaps.core.ui.compose.icons.library.WomanFront
import app.gmaaps.core.ui.compose.icons.library.WomanFrontPaths

enum class BodyType(
    val value: Int,
    val sizeRatio: Float,
    val frontImage: ImageVector,
    val backImage: ImageVector,
    val frontZones: List<Pair<TE.Location, Path>>,
    val backZones: List<Pair<TE.Location, Path>>
) {

    MAN(0, 1f, ManFront, ManBack, ManFrontPaths.zones, ManBackPaths.zones),
    WOMAN(1, 0.95f, WomanFront, WomanBack, WomanFrontPaths.zones, WomanBackPaths.zones),
    CHILD(2, 0.60f, ChildFront, ChildBack, ChildFrontPaths.zones, ChildBackPaths.zones);

    companion object {

        fun fromPref(pref: Int): BodyType = entries.firstOrNull { it.value == pref } ?: MAN
    }
}

