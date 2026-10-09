package app.gmaaps.core.ui.compose.preference

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.gmaaps.core.keys.DoubleKey

@Preview(showBackground = true)
@Composable
internal fun AdaptiveDoublePreferencePreview() {
    PreviewTheme {
        AdaptiveDoublePreferenceItem(
            doubleKey = DoubleKey.OverviewInsulinButtonIncrement1
        )
    }
}

