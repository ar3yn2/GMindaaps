package app.gmaaps.database.persistence.converters

import app.gmaaps.core.data.model.TDD
import app.gmaaps.database.entities.TotalDailyDose

fun TotalDailyDose.fromDb(): TDD =
    TDD(
        id = this.id,
        version = this.version,
        dateCreated = this.dateCreated,
        isValid = this.isValid,
        referenceId = this.referenceId,
        timestamp = this.timestamp,
        utcOffset = this.utcOffset,
        basalAmount = this.basalAmount,
        bolusAmount = this.bolusAmount,
        totalAmount = this.totalAmount,
        carbs = this.carbs,
        carbInsulin = this.carbInsulin,
        ids = this.interfaceIDs.fromDb()
    )

fun TDD.toDb(): TotalDailyDose =
    TotalDailyDose(
        id = this.id,
        version = this.version,
        dateCreated = this.dateCreated,
        isValid = this.isValid,
        referenceId = this.referenceId,
        timestamp = this.timestamp,
        utcOffset = this.utcOffset,
        basalAmount = this.basalAmount,
        bolusAmount = this.bolusAmount,
        totalAmount = this.totalAmount,
        carbs = this.carbs,
        carbInsulin = this.carbInsulin,
        interfaceIDs_backing = this.ids.toDb()
    )

