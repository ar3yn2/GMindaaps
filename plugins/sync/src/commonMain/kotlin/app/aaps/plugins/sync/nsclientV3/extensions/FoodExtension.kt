package app.gmaaps.plugins.sync.nsclientV3.extensions

import app.gmaaps.core.data.model.FD
import app.gmaaps.core.data.model.IDs
import app.gmaaps.core.nssdk.localmodel.food.NSFood
import kotlin.time.Clock

fun NSFood.toFood(): FD =
    FD(
        isValid = isValid,
        name = name,
        category = category,
        subCategory = subCategory,
        portion = portion,
        carbs = carbs,
        fat = fat,
        protein = protein,
        energy = energy,
        unit = unit,
        gi = gi,
        ids = IDs(nightscoutId = identifier)
    )

fun FD.toNSFood(): NSFood =
    NSFood(
        date = Clock.System.now().toEpochMilliseconds(),
        isValid = isValid,
        name = name,
        category = category,
        subCategory = subCategory,
        portion = portion,
        carbs = carbs,
        fat = fat,
        protein = protein,
        energy = energy,
        unit = unit,
        gi = gi,
        identifier = ids.nightscoutId,
    )

