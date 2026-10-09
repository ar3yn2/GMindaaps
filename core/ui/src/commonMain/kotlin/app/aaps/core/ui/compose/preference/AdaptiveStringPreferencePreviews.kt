package app.gmaaps.core.ui.compose.preference

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.gmaaps.core.keys.StringKey

@Preview(showBackground = true)
@Composable
internal fun AdaptiveStringPreferencePreview() {
    PreviewTheme {
        AdaptiveStringPreferenceItem(
            stringKey = StringKey.GeneralPatientName
        )
    }
}

