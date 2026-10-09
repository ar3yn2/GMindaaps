package app.gmaaps.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import app.gmaaps.database.entities.data.Block
import app.gmaaps.database.entities.data.GlucoseUnit
import app.gmaaps.database.entities.data.TargetBlock
import app.gmaaps.database.entities.embedments.InsulinConfiguration
import app.gmaaps.database.entities.embedments.InterfaceIDs
import app.gmaaps.database.entities.interfaces.DBEntryWithTimeAndDuration
import app.gmaaps.database.entities.interfaces.TraceableDBEntry

@Entity(
    tableName = TABLE_PROFILE_SWITCHES,
    foreignKeys = [ForeignKey(
        entity = ProfileSwitch::class,
        parentColumns = ["id"],
        childColumns = ["referenceId"]
    )],
    indices = [
        Index("referenceId"),
        Index("timestamp"),
        Index("nightscoutId")
    ]
)
data class ProfileSwitch(
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    override var version: Int = 0,
    override var dateCreated: Long = -1,
    override var isValid: Boolean = true,
    override var referenceId: Long? = null,
    @Embedded
    override var interfaceIDs_backing: InterfaceIDs? = InterfaceIDs(),
    override var timestamp: Long,
    override var utcOffset: Long = defaultUtcOffset(timestamp),
    var basalBlocks: List<Block>,
    var isfBlocks: List<Block>,
    var icBlocks: List<Block>,
    var targetBlocks: List<TargetBlock>,
    var glucoseUnit: GlucoseUnit,
    var profileName: String,
    var timeshift: Long,  // [milliseconds]
    var percentage: Int, // 1 ~ XXX [%]
    override var duration: Long, // [milliseconds]
    @Embedded
    var insulinConfiguration: InsulinConfiguration
) : TraceableDBEntry, DBEntryWithTimeAndDuration
