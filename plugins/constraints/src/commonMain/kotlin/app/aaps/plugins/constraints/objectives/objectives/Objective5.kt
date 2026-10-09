package app.gmaaps.plugins.constraints.objectives.objectives

import app.gmaaps.core.data.time.T
import app.gmaaps.core.interfaces.resources.TextResolver
import app.gmaaps.core.interfaces.utils.DateUtil
import app.gmaaps.core.keys.interfaces.Preferences
import app.gmaaps.plugins.constraints.ConstraintsStrings
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.IntKey
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metro.binding

@Suppress("SpellCheckingInspection")
// Contributed rather than listed in a graph. ObjectivesPlugin takes List<Objective> and is itself
// contributed to AppScope, so the objectives have to be reachable from the same graph.
@ContributesIntoMap(AppScope::class, binding = binding<Objective>())
@IntKey(5)
@SingleIn(AppScope::class)
@Inject
class Objective5(
    preferences: Preferences,
    rh: TextResolver,
    durationText: DurationText,
    dateUtil: DateUtil,
) : Objective(preferences, rh, dateUtil, durationText, "maxiobzero", ConstraintsStrings.objectives_maxiobzero_objective, ConstraintsStrings.objectives_maxiobzero_gate) {

    init {
        tasks.add(MinimumDurationTask(this, T.days(5).msecs()).learned(Learned(ConstraintsStrings.objectives_maxiobzero_learned)))
    }
}
