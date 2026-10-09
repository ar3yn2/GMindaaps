package app.gmaaps.plugins.sync.tidepool.keys

import app.gmaaps.core.keys.interfaces.StringNonPreferenceKey

enum class TidepoolStringNonKey(
    override val key: String,
    override val defaultValue: String,
) : StringNonPreferenceKey {

    SubscriptionId("tidepool_subscription_id", ""),
    AuthState("tidepool_auth_state", ""),
    ServiceConfiguration("tidepool_service_configuration", "")
}
