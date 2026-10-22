package com.nativelap.ecoguard.feature.notice.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeListItem
import com.nativelap.ecoguard.feature.notice.module.domain.repository.NoticeRepository
import javax.inject.Inject

class GetNoticesUseCase
    @Inject
    constructor(
        private val noticeRepository: NoticeRepository,
    ) {
        suspend operator fun invoke(): AppResult<List<NoticeListItem>> = noticeRepository.getNotices()
    }
