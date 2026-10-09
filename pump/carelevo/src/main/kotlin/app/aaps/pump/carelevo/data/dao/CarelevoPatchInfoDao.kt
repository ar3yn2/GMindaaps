package app.gmaaps.pump.carelevo.data.dao

import app.gmaaps.pump.carelevo.data.model.entities.CarelevoPatchInfoEntity
import io.reactivex.rxjava3.core.Observable
import java.util.Optional

interface CarelevoPatchInfoDao {

    fun getPatchInfo(): Observable<Optional<CarelevoPatchInfoEntity>>
    fun getPatchInfoBySync(): CarelevoPatchInfoEntity?

    fun updatePatchInfo(info: CarelevoPatchInfoEntity): Boolean
    fun deletePatchInfo(): Boolean
}
