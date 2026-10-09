package app.gmaaps.core.ui.compose.preference

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.gmaaps.core.ui.CoreUiStrings

@Preview(showBackground = true)
@Composable
internal fun CollapsibleCardSectionContentPreview() {
    PreviewTheme {
        CollapsibleCardSectionContent(
            title = CoreUiStrings.configbuilder_insulin,
            expanded = true,
            onToggle = {}
        ) {
            Text("Section content", modifier = Modifier.padding(start = 16.dp))
        }
    }
}

