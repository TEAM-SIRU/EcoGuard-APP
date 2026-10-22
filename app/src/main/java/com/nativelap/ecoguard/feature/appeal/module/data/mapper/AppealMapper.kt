package com.nativelap.ecoguard.feature.appeal.module.data.mapper

import com.nativelap.ecoguard.feature.appeal.module.data.dto.AppealDecisionRequestDto
import com.nativelap.ecoguard.feature.appeal.module.data.dto.AppealResponseDto
import com.nativelap.ecoguard.feature.appeal.module.data.dto.MyAppealResponseDto
import com.nativelap.ecoguard.feature.appeal.module.domain.model.Appeal
import com.nativelap.ecoguard.feature.appeal.module.domain.model.AppealDecision
import com.nativelap.ecoguard.feature.appeal.module.domain.model.AppealStatus
import com.nativelap.ecoguard.feature.appeal.module.domain.model.MyAppeal

fun AppealResponseDto.toDomain(): Appeal =
    Appeal(
        appealId = appealId,
        verificationId = verificationId,
        round = round,
        studentName = studentName,
        studentNumber = studentNumber,
        areaName = areaName,
        verificationDate = verificationDate,
        photoUrl = photoUrl,
        failReasons = failReasons,
        content = content,
        photoUrls = photoUrls,
        status = status.toAppealStatus(),
        replyTitle = replyTitle,
        reply = reply,
        createdAt = createdAt,
    )

fun MyAppealResponseDto.toDomain(): MyAppeal =
    MyAppeal(
        appealId = appealId,
        verificationId = verificationId,
        round = round,
        areaName = areaName,
        verificationDate = verificationDate,
        content = content,
        photoUrls = photoUrls,
        status = status.toAppealStatus(),
        awardedMinutes = awardedMinutes,
        replyTitle = replyTitle,
        reply = reply,
        createdAt = createdAt,
    )

fun AppealDecision.toRequestDto(): AppealDecisionRequestDto =
    AppealDecisionRequestDto(
        decision = decision,
        reply = reply,
        replyTitle = replyTitle,
    )

private fun String?.toAppealStatus(): AppealStatus? =
    when (this) {
        null -> null
        "PENDING" -> AppealStatus.Pending
        "APPROVED" -> AppealStatus.Approved
        "REJECTED" -> AppealStatus.Rejected
        else -> AppealStatus.Unknown(this)
    }
