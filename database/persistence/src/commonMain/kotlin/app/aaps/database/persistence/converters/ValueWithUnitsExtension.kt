package app.gmaaps.database.persistence.converters

import app.gmaaps.core.data.ue.ValueWithUnit

fun app.gmaaps.database.entities.ValueWithUnit.fromDb(): app.gmaaps.core.data.ue.ValueWithUnit =
    when (this) {
        is app.gmaaps.database.entities.ValueWithUnit.Gram                  -> ValueWithUnit.Gram(value)
        is app.gmaaps.database.entities.ValueWithUnit.Hour                  -> ValueWithUnit.Hour(value)
        is app.gmaaps.database.entities.ValueWithUnit.Insulin               -> ValueWithUnit.Insulin(value)
        is app.gmaaps.database.entities.ValueWithUnit.InsulinConcentration  -> ValueWithUnit.InsulinConcentration(value)
        is app.gmaaps.database.entities.ValueWithUnit.Mgdl                  -> ValueWithUnit.Mgdl(value)
        is app.gmaaps.database.entities.ValueWithUnit.Minute                -> ValueWithUnit.Minute(value)
        is app.gmaaps.database.entities.ValueWithUnit.Mmoll                 -> ValueWithUnit.Mmoll(value)
        is app.gmaaps.database.entities.ValueWithUnit.RunningModeMode       -> ValueWithUnit.RMMode(value.fromDb())
        is app.gmaaps.database.entities.ValueWithUnit.Percent               -> ValueWithUnit.Percent(value)
        is app.gmaaps.database.entities.ValueWithUnit.SimpleInt             -> ValueWithUnit.SimpleInt(value)
        is app.gmaaps.database.entities.ValueWithUnit.SimpleString          -> ValueWithUnit.SimpleString(value)
        is app.gmaaps.database.entities.ValueWithUnit.TherapyEventMeterType -> ValueWithUnit.TEMeterType(value.fromDb())
        is app.gmaaps.database.entities.ValueWithUnit.TherapyEventTTReason  -> ValueWithUnit.TETTReason(value.fromDb())
        is app.gmaaps.database.entities.ValueWithUnit.TherapyEventType      -> ValueWithUnit.TEType(value.fromDb())
        is app.gmaaps.database.entities.ValueWithUnit.Timestamp             -> ValueWithUnit.Timestamp(value)
        is app.gmaaps.database.entities.ValueWithUnit.UnitPerHour           -> ValueWithUnit.UnitPerHour(value)
        is app.gmaaps.database.entities.ValueWithUnit.TherapyEventLocation  -> ValueWithUnit.TELocation(value.fromDb())
        is app.gmaaps.database.entities.ValueWithUnit.TherapyEventArrow     -> ValueWithUnit.TEArrow(value.fromDb())
        is app.gmaaps.database.entities.ValueWithUnit.UNKNOWN               -> ValueWithUnit.UNKNOWN
    }

fun app.gmaaps.core.data.ue.ValueWithUnit.toDb(): app.gmaaps.database.entities.ValueWithUnit =
    when (this) {
        is ValueWithUnit.Gram                 -> app.gmaaps.database.entities.ValueWithUnit.Gram(value)
        is ValueWithUnit.Hour                 -> app.gmaaps.database.entities.ValueWithUnit.Hour(value)
        is ValueWithUnit.Insulin              -> app.gmaaps.database.entities.ValueWithUnit.Insulin(value)
        is ValueWithUnit.InsulinConcentration -> app.gmaaps.database.entities.ValueWithUnit.InsulinConcentration(value)
        is ValueWithUnit.Mgdl                 -> app.gmaaps.database.entities.ValueWithUnit.Mgdl(value)
        is ValueWithUnit.Minute               -> app.gmaaps.database.entities.ValueWithUnit.Minute(value)
        is ValueWithUnit.Mmoll                -> app.gmaaps.database.entities.ValueWithUnit.Mmoll(value)
        is ValueWithUnit.RMMode               -> app.gmaaps.database.entities.ValueWithUnit.RunningModeMode(value.toDb())
        is ValueWithUnit.Percent              -> app.gmaaps.database.entities.ValueWithUnit.Percent(value)
        is ValueWithUnit.SimpleInt            -> app.gmaaps.database.entities.ValueWithUnit.SimpleInt(value)
        is ValueWithUnit.SimpleString         -> app.gmaaps.database.entities.ValueWithUnit.SimpleString(value)
        is ValueWithUnit.TEMeterType          -> app.gmaaps.database.entities.ValueWithUnit.TherapyEventMeterType(value.toDb())
        is ValueWithUnit.TETTReason           -> app.gmaaps.database.entities.ValueWithUnit.TherapyEventTTReason(value.toDb())
        is ValueWithUnit.TEType               -> app.gmaaps.database.entities.ValueWithUnit.TherapyEventType(value.toDb())
        is ValueWithUnit.Timestamp            -> app.gmaaps.database.entities.ValueWithUnit.Timestamp(value)
        is ValueWithUnit.UnitPerHour          -> app.gmaaps.database.entities.ValueWithUnit.UnitPerHour(value)
        is ValueWithUnit.TEArrow              -> app.gmaaps.database.entities.ValueWithUnit.TherapyEventArrow(value.toDb())
        is ValueWithUnit.TELocation           -> app.gmaaps.database.entities.ValueWithUnit.TherapyEventLocation(value.toDb())
        is ValueWithUnit.UNKNOWN              -> app.gmaaps.database.entities.ValueWithUnit.UNKNOWN
    }

fun List<app.gmaaps.database.entities.ValueWithUnit>.fromDb(): List<app.gmaaps.core.data.ue.ValueWithUnit> =
    this.map { it.fromDb() }

fun List<app.gmaaps.core.data.ue.ValueWithUnit>.toDb(): List<app.gmaaps.database.entities.ValueWithUnit> =
    this.map { it.toDb() }

