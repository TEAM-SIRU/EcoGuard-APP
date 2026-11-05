package com.nativelap.ecoguard.feature.appeal.viewmodel

import com.nativelap.ecoguard.feature.appeal.module.domain.model.AppealStatus
import com.nativelap.ecoguard.feature.appeal.module.domain.model.MyAppeal
import com.nativelap.ecoguard.ui.component.StatusChipType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AppealHistoryMappingTest {
    @Test
    fun knownStatusesAndRepliesComeFromServer() {
        assertEquals(StatusChipType.REVIEWING, appeal(AppealStatus.Pending).toUiModel()?.status)
        assertEquals(StatusChipType.APPROVED, appeal(AppealStatus.Approved).toUiModel()?.status)
        val rejected = appeal(AppealStatus.Rejected).copy(replyTitle = "답변", reply = "확인했습니다").toUiModel()
        assertEquals(StatusChipType.REJECTED, rejected?.status)
        assertEquals("답변", rejected?.replyTitle)
        assertEquals("확인했습니다", rejected?.reply)
        assertNull(appeal(AppealStatus.Rejected).copy(reply = " ").toUiModel()?.reply)
    }

    @Test
    fun nonPositiveAppealIdDoesNotCreateDetailItem() {
        assertNull(appeal(AppealStatus.Pending).copy(appealId = 0).toUiModel())
        assertNull(appeal(AppealStatus.Pending).copy(appealId = -1).toUiModel())
    }

    @Test
    fun unknownStatusDoesNotInventResult() {
        assertNull(appeal(AppealStatus.Unknown("NEW")).toUiModel())
    }

    private fun appeal(status: AppealStatus) =
        MyAppeal(
            appealId = 3,
            verificationId = 5,
            round = 1,
            areaName = "복도",
            verificationDate = "2026-10-25",
            content = "내용",
            photoUrls = emptyList(),
            status = status,
            awardedMinutes = null,
            replyTitle = null,
            reply = null,
            createdAt = "2026-10-25T08:00:00",
        )
}
