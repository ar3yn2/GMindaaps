package app.gmaaps.database.persistence.converters

import app.gmaaps.core.data.model.ICfg
import app.gmaaps.database.entities.embedments.InsulinConfiguration

fun InsulinConfiguration.fromDb(): ICfg =
    ICfg(
        insulinLabel = this.insulinLabel,
        insulinEndTime = this.insulinEndTime,
        insulinPeakTime = this.insulinPeakTime,
        concentration = this.concentration
    )

fun ICfg.toDb(): InsulinConfiguration =
    InsulinConfiguration(
        insulinLabel = this.insulinLabel,
        insulinEndTime = this.insulinEndTime,
        insulinPeakTime = this.insulinPeakTime,
        concentration = this.concentration
    )
