package app.gmaaps.core.ui.compose.preference

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.gmaaps.core.keys.IntKey

@Preview(showBackground = true)
@Composable
internal fun AdaptiveIntPreferencePreview() {
    PreviewTheme {
        AdaptiveIntPreferenceItem(
            intKey = IntKey.OverviewCarbsButtonIncrement1
        )
    }
}

