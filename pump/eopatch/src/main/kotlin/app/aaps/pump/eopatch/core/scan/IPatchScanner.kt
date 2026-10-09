package app.gmaaps.pump.eopatch.core.scan

import io.reactivex.rxjava3.core.Single

interface IPatchScanner {
    fun scan(timeout: Long): Single<ScanList>
}

