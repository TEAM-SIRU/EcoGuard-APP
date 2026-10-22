package com.nativelap.ecoguard.feature.appeal.module.domain.repository

import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.appeal.module.domain.model.Appeal
import com.nativelap.ecoguard.feature.appeal.module.domain.model.AppealDecision
import com.nativelap.ecoguard.feature.appeal.module.domain.model.AppealStatus
import com.nativelap.ecoguard.feature.appeal.module.domain.model.MyAppeal
import java.io.File

interface AppealRepository {
    suspend fun createAppealWithPhotos(
        verificationId: Long,
        content: String,
        photos: List<File>,
    ): AppResult<Unit>

    suspend fun createAppeal(
        verificationId: Long,
        content: String,
    ): AppResult<Unit>

    suspend fun getAppeals(status: AppealStatus?): AppResult<List<Appeal>>

    suspend fun getMyAppeals(): AppResult<List<MyAppeal>>

    suspend fun decideAppeal(
        appealId: Long,
        decision: AppealDecision,
    ): AppResult<Unit>
}
