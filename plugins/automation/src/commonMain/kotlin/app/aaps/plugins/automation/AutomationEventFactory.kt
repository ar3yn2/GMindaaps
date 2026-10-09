package app.gmaaps.plugins.automation

import app.gmaaps.core.interfaces.logging.AAPSLogger
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.plugins.automation.actions.ActionFactory
import app.gmaaps.plugins.automation.triggers.TriggerDeps
import app.gmaaps.plugins.automation.triggers.TriggerFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

/**
 * Builds [AutomationEventObject]s and holds what they need.
 * This holds the dependencies instead and passes them in. The event reads them back through the
 * factory, so a new event can be created from an existing one without carrying a second bundle.
 */
@SingleIn(AppScope::class)
@Inject
class AutomationEventFactory(
    val aapsLogger: AAPSLogger,
    val dateUtil: DateUtil,
    val actionFactory: ActionFactory,
    val triggerFactory: TriggerFactory,
    val triggerDeps: TriggerDeps
) {

    /** A new, empty event. */
    fun newEvent(): AutomationEventObject = AutomationEventObject(this)

    /** An event rebuilt from its stored form. */
    fun fromJSON(data: String): AutomationEventObject = AutomationEventObject(this).fromJSON(data)
}

