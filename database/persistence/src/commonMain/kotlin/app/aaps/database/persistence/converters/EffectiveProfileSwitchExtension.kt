package app.gmaaps.database.persistence.converters

import app.gmaaps.core.data.model.EPS
import app.gmaaps.core.data.model.data.Block
import app.gmaaps.core.data.model.data.TargetBlock
import app.gmaaps.database.entities.EffectiveProfileSwitch

fun EffectiveProfileSwitch.fromDb(): EPS =
    EPS(
        id = this.id,
        version = this.version,
        dateCreated = this.dateCreated,
        isValid = this.isValid,
        referenceId = this.referenceId,
        timestamp = this.timestamp,
        utcOffset = this.utcOffset,
        ids = this.interfaceIDs.fromDb(),
        basalBlocks = mutableListOf<Block>().also { list -> this.basalBlocks.forEach { list.add(it.fromDb()) } },
        isfBlocks = mutableListOf<Block>().also { list -> this.isfBlocks.forEach { list.add(it.fromDb()) } },
        icBlocks = mutableListOf<Block>().also { list -> this.icBlocks.forEach { list.add(it.fromDb()) } },
        targetBlocks = mutableListOf<TargetBlock>().also { list -> this.targetBlocks.forEach { list.add(it.fromDb()) } },
        glucoseUnit = this.glucoseUnit.fromDb(),
        originalProfileName = this.originalProfileName,
        originalCustomizedName = this.originalCustomizedName,
        originalTimeshift = this.originalTimeshift,
        originalPercentage = this.originalPercentage,
        originalDuration = this.originalDuration,
        originalEnd = this.originalEnd,
        originalPsId = this.originalPsId,
        iCfg = this.insulinConfiguration.fromDb()
    )

fun EPS.toDb(): EffectiveProfileSwitch =
    EffectiveProfileSwitch(
        id = this.id,
        version = this.version,
        dateCreated = this.dateCreated,
        isValid = this.isValid,
        referenceId = this.referenceId,
        timestamp = this.timestamp,
        utcOffset = this.utcOffset,
        interfaceIDs_backing = this.ids.toDb(),
        basalBlocks = mutableListOf<app.gmaaps.database.entities.data.Block>().also { list -> this.basalBlocks.forEach { list.add(it.toDb()) } },
        isfBlocks = mutableListOf<app.gmaaps.database.entities.data.Block>().also { list -> this.isfBlocks.forEach { list.add(it.toDb()) } },
        icBlocks = mutableListOf<app.gmaaps.database.entities.data.Block>().also { list -> this.icBlocks.forEach { list.add(it.toDb()) } },
        targetBlocks = mutableListOf<app.gmaaps.database.entities.data.TargetBlock>().also { list -> this.targetBlocks.forEach { list.add(it.toDb()) } },
        glucoseUnit = this.glucoseUnit.toDb(),
        originalProfileName = this.originalProfileName,
        originalCustomizedName = this.originalCustomizedName,
        originalTimeshift = this.originalTimeshift,
        originalPercentage = this.originalPercentage,
        originalDuration = this.originalDuration,
        originalEnd = this.originalEnd,
        originalPsId = this.originalPsId,
        insulinConfiguration = this.iCfg.toDb()
    )

