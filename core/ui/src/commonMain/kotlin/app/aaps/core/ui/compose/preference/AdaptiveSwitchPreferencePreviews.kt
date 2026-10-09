package app.gmaaps.core.ui.compose.preference

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.gmaaps.core.keys.BooleanKey

@Preview(showBackground = true)
@Composable
internal fun AdaptiveSwitchPreferencePreview() {
    PreviewTheme {
        AdaptiveSwitchPreferenceItem(
            booleanKey = BooleanKey.OverviewKeepScreenOn
        )
    }
}

