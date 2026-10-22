package com.nativelap.ecoguard.feature.notice.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notice.module.domain.model.NoticeDetail
import com.nativelap.ecoguard.feature.notice.module.domain.repository.NoticeRepository
import javax.inject.Inject

class GetNoticeDetailUseCase
    @Inject
    constructor(
        private val noticeRepository: NoticeRepository,
    ) {
        suspend operator fun invoke(noticeId: Long): AppResult<NoticeDetail> =
            noticeRepository.getNoticeDetail(noticeId)
    }
