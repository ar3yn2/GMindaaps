package app.gmaaps.core.data.aps

import app.gmaaps.core.data.model.TDD

data class AverageTDD(
    var data: TDD,
    val allDaysHaveCarbs: Boolean
)
