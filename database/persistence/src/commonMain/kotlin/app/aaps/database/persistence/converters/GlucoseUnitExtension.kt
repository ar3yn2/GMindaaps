package app.gmaaps.database.persistence.converters

import app.gmaaps.core.data.model.GlucoseUnit

fun app.gmaaps.database.entities.data.GlucoseUnit.fromDb(): GlucoseUnit =
    when (this) {
        app.gmaaps.database.entities.data.GlucoseUnit.MGDL -> GlucoseUnit.MGDL
        app.gmaaps.database.entities.data.GlucoseUnit.MMOL -> GlucoseUnit.MMOL
    }

fun GlucoseUnit.toDb(): app.gmaaps.database.entities.data.GlucoseUnit =
    when (this) {
        GlucoseUnit.MGDL -> app.gmaaps.database.entities.data.GlucoseUnit.MGDL
        GlucoseUnit.MMOL -> app.gmaaps.database.entities.data.GlucoseUnit.MMOL
    }


