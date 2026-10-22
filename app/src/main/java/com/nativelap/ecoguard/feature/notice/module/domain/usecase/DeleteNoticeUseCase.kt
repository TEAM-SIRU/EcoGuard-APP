package com.nativelap.ecoguard.feature.notice.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notice.module.domain.repository.NoticeRepository
import javax.inject.Inject

class DeleteNoticeUseCase
    @Inject
    constructor(
        private val noticeRepository: NoticeRepository,
    ) {
        suspend operator fun invoke(noticeId: Long): AppResult<Unit> = noticeRepository.deleteNotice(noticeId)
    }
