package app.gmaaps.plugins.constraints.signatureVerifier.keys

import app.gmaaps.core.keys.interfaces.LongNonPreferenceKey

enum class SignatureVerifierLongKey(
    override val key: String,
    override val defaultValue: Long,
) : LongNonPreferenceKey {

    LastRevokedCertCheck("last_revoked_certs_check", 0L),
}

