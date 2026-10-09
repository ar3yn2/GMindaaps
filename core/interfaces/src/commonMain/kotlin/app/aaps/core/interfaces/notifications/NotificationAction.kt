package app.gmaaps.core.interfaces.notifications

import app.gmaaps.core.keys.interfaces.TextRef

data class NotificationAction(
    val buttonText: TextRef,
    val action: () -> Unit
)

