package app.gmaaps.plugins.configuration.setupwizard

import androidx.compose.runtime.Composable
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.keys.interfaces.TextRef
import app.gmaaps.core.ui.compose.stringResource
import app.gmaaps.plugins.configuration.setupwizard.elements.SWItem
import dev.zacsweers.metro.Inject

@Inject
class SWScreen(private val rh: TextResolver) {

    var header: TextRef? = null
        private set

    var items: MutableList<SWItem> = ArrayList()
    var validator: (() -> Boolean)? = null
    var visibility: (() -> Boolean)? = null
    var skippable = false

    fun with(header: TextRef): SWScreen {
        this.header = header
        return this
    }

    /** Convenience for the many call sites that pass their own module's `ConfigurationStrings.x`. */
    fun with(header: Int): SWScreen = with(TextRef.AndroidRes(header))

    fun getHeader(): String = header?.let { rh.gs(it) } ?: ""

    @Composable
    fun getHeaderCompose(): String = header?.let { stringResource(it) } ?: ""

    fun skippable(skippable: Boolean): SWScreen {
        this.skippable = skippable
        return this
    }

    fun add(newItem: SWItem): SWScreen {
        items.add(newItem)
        return this
    }

    fun validator(validator: () -> Boolean): SWScreen {
        this.validator = validator
        return this
    }

    fun visibility(visibility: () -> Boolean): SWScreen {
        this.visibility = visibility
        return this
    }

    @Composable
    fun Compose() {
        items.forEach { item ->
            item.Compose()
        }
    }
}
