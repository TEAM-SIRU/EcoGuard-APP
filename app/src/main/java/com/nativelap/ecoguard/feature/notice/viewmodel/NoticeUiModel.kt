package com.nativelap.ecoguard.feature.notice.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
data class NoticeUiModel(
    val noticeId: Long,
    val title: String,
    val publishedDate: String,
    val body: String,
)
