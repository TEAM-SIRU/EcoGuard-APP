package com.nativelap.ecoguard.feature.notice

import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeDetail
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeListItem
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeDetailUiState
import com.nativelap.ecoguard.feature.notice.viewmodel.NoticeUiState
import com.nativelap.ecoguard.feature.notice.viewmodel.toNoticeDetailUiState
import com.nativelap.ecoguard.feature.notice.viewmodel.toNoticeUiState
import com.nativelap.ecoguard.feature.notice.viewmodel.toUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class NoticeUiMapperTest {
    @Test
    fun `notice list distinguishes empty content and failure`() {
        assertEquals(NoticeUiState.Empty, AppResult.Success(emptyList<NoticeListItem>()).toNoticeUiState())

        val item = NoticeListItem(7, "Title", "Preview", "2026-10-25T12:00:00", false)
        assertEquals(NoticeUiState.Content(listOf(item.toUiModel())), AppResult.Success(listOf(item)).toNoticeUiState())
        assertEquals(NoticeUiState.Failed, AppResult.Failure(AppError.Connectivity).toNoticeUiState())
    }

    @Test
    fun `notice detail maps missing resource separately from other failures`() {
        assertEquals(NoticeDetailUiState.Missing, AppResult.Failure(AppError.Http(404)).toNoticeDetailUiState())
        assertEquals(NoticeDetailUiState.Failed, AppResult.Failure(AppError.Connectivity).toNoticeDetailUiState())
        val detail = NoticeDetail(7, "Title", "Body", "2026-10-25T12:00:00", null, null)
        assertEquals(
            NoticeDetailUiState.Content(detail.toUiModel()),
            AppResult.Success(detail).toNoticeDetailUiState(),
        )
    }
}
