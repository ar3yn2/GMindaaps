package app.gmaaps.wear.preference

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import app.gmaaps.wear.BuildConfig

/**
 * Preference for displaying the app version
 */
@Suppress("unused")
class VersionPreference(context: Context, attrs: AttributeSet?) : WearListPreference(context, attrs) {

    override fun getSummaryText(context: Context): CharSequence {
        return BuildConfig.BUILDVERSION
    }

    override fun onPreferenceClick(context: Context) {
        Toast.makeText(context, "Build version:" + BuildConfig.BUILDVERSION, Toast.LENGTH_LONG).show()
    }

    init {
        entries = arrayOf<CharSequence>(BuildConfig.BUILDVERSION)
        entryValues = arrayOf<CharSequence>(BuildConfig.BUILDVERSION)
    }
}
