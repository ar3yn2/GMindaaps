package app.gmaaps.database.persistence.converters

import app.gmaaps.core.data.model.data.Block

fun app.gmaaps.database.entities.data.Block.fromDb(): Block = Block(duration = this.duration, amount = this.amount)
fun Block.toDb(): app.gmaaps.database.entities.data.Block = app.gmaaps.database.entities.data.Block(duration = this.duration, amount = this.amount)

