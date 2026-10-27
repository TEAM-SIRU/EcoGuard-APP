package com.nativelap.ecoguard.feature.activity

import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivityRecord
import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivityResultStatus
import com.nativelap.ecoguard.feature.activity.module.domain.model.ActivitySummary
import com.nativelap.ecoguard.feature.activity.module.domain.model.MyActivity
import com.nativelap.ecoguard.feature.activity.viewmodel.ActivityUiState
import com.nativelap.ecoguard.feature.activity.viewmodel.toActivityUiState
import com.nativelap.ecoguard.ui.component.StatusChipType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class ActivityUiMapperTest {
    @Test
    fun `maps summary and groups records by server dates`() {
        val activity =
            MyActivity(
                totalMinutes = 40,
                year = 2026,
                month = 10,
                monthlyMinutes = 10,
                summary = ActivitySummary(1, 5, 1, 0, 1),
                records =
                    listOf(
                        ActivityRecord("2026-10-23", "본관 복도", ActivityResultStatus.APPROVED, 20, null, 10),
                        ActivityRecord("2026-10-22", null, ActivityResultStatus.NOT_SUBMITTED, null, null, 0),
                    ),
            )

        val uiState = activity.toActivityUiState(LocalDate.parse("2026-10-25"))

        assertTrue(uiState is ActivityUiState.Content)
        val content = uiState as ActivityUiState.Content
        assertEquals(10, content.monthlyMinutes)
        assertEquals(1, content.approvedCount)
        assertEquals(1, content.notSubmittedCount)
        assertEquals(1, content.weekGroups.size)
        assertEquals(
            StatusChipType.APPROVED,
            content.weekGroups
                .single()
                .records
                .first()
                .status,
        )
        assertEquals(
            null,
            content.weekGroups
                .single()
                .records
                .last()
                .recordId,
        )
    }

    @Test
    fun `unknown status is not presented as a known status`() {
        val uiState =
            activityWith(
                ActivityRecord("2026-10-23", "본관 복도", ActivityResultStatus.UNKNOWN, null, null, 0),
            ).toActivityUiState(LocalDate.parse("2026-10-25"))

        assertNull(uiState)
    }

    @Test
    fun `empty server records produce an empty content state`() {
        val uiState = activityWith().toActivityUiState(LocalDate.parse("2026-10-25"))

        assertTrue(uiState is ActivityUiState.Content)
        assertEquals(
            emptyList<com.nativelap.ecoguard.feature.activity.viewmodel.ActivityWeekGroupUiModel>(),
            (uiState as ActivityUiState.Content).weekGroups,
        )
    }

    private fun activityWith(vararg records: ActivityRecord) =
        MyActivity(
            totalMinutes = 0,
            year = 2026,
            month = 10,
            monthlyMinutes = 0,
            summary = ActivitySummary(0, 0, 0, 0, 0),
            records = records.toList(),
        )
}
