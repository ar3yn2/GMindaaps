package app.gmaaps.core.ui.compose.siteRotation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.gmaaps.core.data.model.TE
import app.gmaaps.core.ui.compose.icons.IcCannulaChange
import app.gmaaps.core.ui.compose.icons.IcCgmInsert

@Preview(showBackground = true)
@Composable
internal fun SiteEntryListPreview() {
    MaterialTheme {
        SiteEntryList(
            entries = listOf(
                SiteEntryDisplayData(
                    typeIcon = IcCannulaChange,
                    dateString = "10/03/2026",
                    locationString = "Left Abdomen",
                    arrowIcon = TE.Arrow.UP.directionToComposeIcon(),
                    note = "Rotated clockwise",
                    timestamp = 1741600000000L,
                    location = TE.Location.FRONT_LEFT_UPPER_ABDOMEN
                ),
                SiteEntryDisplayData(
                    typeIcon = IcCgmInsert,
                    dateString = "08/03/2026",
                    locationString = "Right Arm",
                    arrowIcon = TE.Arrow.NONE.directionToComposeIcon(),
                    note = null,
                    timestamp = 1741400000000L,
                    location = TE.Location.SIDE_RIGHT_UPPER_ARM
                )
            ),
            showEditButton = true,
            onEntryClick = {},
            onEditClick = {}
        )
    }
}

