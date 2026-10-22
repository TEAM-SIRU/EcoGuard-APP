package com.nativelap.ecoguard.feature.notice.module.domain.usecase

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.notice.module.domain.model.CreatedNotice
import com.nativelap.ecoguard.feature.notice.module.domain.repository.NoticeRepository
import javax.inject.Inject

class CreateNoticeUseCase
    @Inject
    constructor(
        private val noticeRepository: NoticeRepository,
    ) {
        suspend operator fun invoke(
            title: String?,
            content: String?,
        ): AppResult<CreatedNotice> = noticeRepository.createNotice(title, content)
    }
