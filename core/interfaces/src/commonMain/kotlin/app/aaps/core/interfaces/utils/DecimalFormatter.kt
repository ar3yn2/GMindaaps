package app.gmaaps.core.interfaces.utils

import app.gmaaps.core.data.format.NumberFormat
import app.gmaaps.core.interfaces.pump.PumpInsulin

/**
 * Format double values to strings
 */
interface DecimalFormatter {

    fun to0Decimal(value: Double): String
    fun to0Decimal(value: Double, unit: String): String
    fun to1Decimal(value: Double): String
    fun to1Decimal(value: Double, unit: String): String
    fun to2Decimal(value: Double): String
    fun to2Decimal(value: Double, unit: String): String
    fun to3Decimal(value: Double): String
    fun to3Decimal(value: Double, unit: String): String
    fun toPumpSupportedBolus(value: Double, bolusStep: Double): String
    fun toPumpSupportedBolusWithUnits(value: Double, bolusStep: Double): String
    fun toPumpSupportedBolusWithUnits(value: PumpInsulin, bolusStep: Double): String
    fun pumpSupportedBolusFormat(bolusStep: Double): NumberFormat
}
