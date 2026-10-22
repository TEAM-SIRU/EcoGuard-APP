package com.nativelap.ecoguard.feature.notice.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notice.module.domain.repository.NoticeRepository
import javax.inject.Inject

class UpdateNoticeUseCase
    @Inject
    constructor(
        private val noticeRepository: NoticeRepository,
    ) {
        suspend operator fun invoke(
            noticeId: Long,
            title: String?,
            content: String?,
        ): AppResult<Unit> = noticeRepository.updateNotice(noticeId, title, content)
    }
