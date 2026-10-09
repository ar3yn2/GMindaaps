package app.gmaaps.core.interfaces.maintenance

import androidx.documentfile.provider.DocumentFile
import app.gmaaps.core.interfaces.rx.weardata.CwfFile
import java.io.File

interface FileListProvider : PrefsFileInfo {

    val resultPath: File
    fun ensurePreferenceDirExists(): DocumentFile?
    fun ensureExportDirExists(): DocumentFile?
    fun ensureTempDirExists(): DocumentFile?
    fun ensureExtraDirExists(): DocumentFile?

    fun newPreferenceFile(): DocumentFile?
    fun newExportCsvFile(): DocumentFile?
    fun newCwfFile(filename: String, withDate: Boolean = true): DocumentFile?

    fun ensureResultDirExists(): File
    fun newResultFile(): File
    fun listCustomWatchfaceFiles(): MutableList<CwfFile>
    fun checkMetadata(metadata: Map<PrefsMetadataKey, PrefMetadata>): Map<PrefsMetadataKey, PrefMetadata>
}
