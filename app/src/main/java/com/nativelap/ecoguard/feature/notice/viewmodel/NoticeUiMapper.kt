package com.nativelap.ecoguard.feature.notice.viewmodel

import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeDetail
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeListItem
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun AppResult<List<NoticeListItem>>.toNoticeUiState(): NoticeUiState =
    when (this) {
        is AppResult.Success -> {
            if (value.isEmpty()) {
                NoticeUiState.Empty
            } else {
                NoticeUiState.Content(value.map { it.toUiModel() })
            }
        }

        is AppResult.Failure -> {
            NoticeUiState.Failed
        }
    }

internal fun AppResult<NoticeDetail>.toNoticeDetailUiState(): NoticeDetailUiState =
    when (this) {
        is AppResult.Success -> {
            NoticeDetailUiState.Content(value.toUiModel())
        }

        is AppResult.Failure -> {
            if (error == AppError.Http(NOT_FOUND_STATUS)) {
                NoticeDetailUiState.Missing
            } else {
                NoticeDetailUiState.Failed
            }
        }
    }

fun NoticeListItem.toUiModel(): NoticeUiModel =
    NoticeUiModel(
        noticeId = noticeId,
        title = title,
        publishedDate = createdAt.toDisplayDate(),
        body = preview,
        isRead = isRead,
    )

fun NoticeDetail.toUiModel(): NoticeUiModel =
    NoticeUiModel(
        noticeId = noticeId,
        title = title,
        publishedDate = createdAt.toDisplayDate(),
        body = content,
    )

private fun String.toDisplayDate(): String =
    runCatching { LocalDateTime.parse(this).format(DATE_FORMATTER) }
        .getOrDefault(this)

private val DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
private const val NOT_FOUND_STATUS = 404
