package app.gmaaps.core.ui.compose

import androidx.compose.ui.text.PlatformTextStyle

actual fun noFontPaddingPlatformStyle(): PlatformTextStyle? = PlatformTextStyle(includeFontPadding = false)

