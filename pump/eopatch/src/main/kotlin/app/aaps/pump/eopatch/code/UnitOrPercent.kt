package app.gmaaps.pump.eopatch.code

enum class UnitOrPercent {
    P,
    U;

    fun isPercentage() = this == P
}

