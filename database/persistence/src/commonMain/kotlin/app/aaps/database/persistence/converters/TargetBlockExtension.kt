package app.gmaaps.database.persistence.converters

import app.gmaaps.core.data.model.data.TargetBlock

fun app.gmaaps.database.entities.data.TargetBlock.fromDb(): TargetBlock = TargetBlock(duration = this.duration, lowTarget = this.lowTarget, highTarget = this.highTarget)
fun TargetBlock.toDb(): app.gmaaps.database.entities.data.TargetBlock = app.gmaaps.database.entities.data.TargetBlock(duration = this.duration, lowTarget = this.lowTarget, highTarget = this.highTarget)

