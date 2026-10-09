package app.gmaaps.core.ui.compose.preference

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.gmaaps.core.keys.IntKey

@Preview(showBackground = true)
@Composable
internal fun AdaptiveListIntPreferencePreview() {
    PreviewTheme {
        AdaptiveListIntPreferenceItem(
            intKey = IntKey.OverviewCarbsButtonIncrement1,
            entries = listOf("5g", "10g", "15g", "20g"),
            entryValues = listOf(5, 10, 15, 20)
        )
    }
}

