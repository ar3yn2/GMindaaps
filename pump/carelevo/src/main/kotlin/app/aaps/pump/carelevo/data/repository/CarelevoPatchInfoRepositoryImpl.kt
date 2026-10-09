package app.gmaaps.pump.carelevo.data.repository

import app.gmaaps.pump.carelevo.data.dataSource.local.CarelevoPatchInfoDataSource
import app.gmaaps.pump.carelevo.data.mapper.transformToCarelevoPatchInfoDomainModel
import app.gmaaps.pump.carelevo.data.mapper.transformToCarelevoPatchInfoEntity
import app.gmaaps.pump.carelevo.domain.model.patch.CarelevoPatchInfoDomainModel
import app.gmaaps.pump.carelevo.domain.repository.CarelevoPatchInfoRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import io.reactivex.rxjava3.core.Observable
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

@ContributesBinding(AppScope::class)
class CarelevoPatchInfoRepositoryImpl @Inject constructor(
    private val patchInfoDataSource: CarelevoPatchInfoDataSource
) : CarelevoPatchInfoRepository {

    override fun getPatchInfo(): Observable<Optional<CarelevoPatchInfoDomainModel>> {
        return patchInfoDataSource.getPatchInfo()
            .map { Optional.ofNullable(it.getOrNull()?.transformToCarelevoPatchInfoDomainModel()) }
    }

    override fun getPatchInfoBySync(): CarelevoPatchInfoDomainModel? {
        return patchInfoDataSource.getPatchInfoBySync()?.transformToCarelevoPatchInfoDomainModel()
    }

    override fun updatePatchInfo(info: CarelevoPatchInfoDomainModel): Boolean {
        return patchInfoDataSource.updatePatchInfo(info.transformToCarelevoPatchInfoEntity())
    }

    override fun deletePatchInfo(): Boolean {
        return patchInfoDataSource.deletePatchInfo()
    }
}
