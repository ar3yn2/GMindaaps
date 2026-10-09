package app.gmaaps.pump.eopatch.extension

fun <T> Boolean.takeOne(whenTrue: T, whenFalse: T): T {
    return if (this) whenTrue else whenFalse
}

