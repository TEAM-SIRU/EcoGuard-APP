package com.nativelap.ecoguard.feature.appeal.view

import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.core.domain.result.AppError
import com.nativelap.ecoguard.core.domain.result.AppResult
import com.nativelap.ecoguard.feature.appeal.module.domain.model.AppealStatus
import com.nativelap.ecoguard.feature.appeal.module.domain.model.MyAppeal
import com.nativelap.ecoguard.feature.appeal.module.domain.repository.AppealRepository
import com.nativelap.ecoguard.feature.appeal.module.domain.usecase.GetMyAppealsUseCase
import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealHistoryViewModel
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AppealResultRouteTest {
    @get:Rule val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun showsLoadingThenServerResult() {
        val response = CompletableDeferred<AppResult<List<MyAppeal>>>()
        val viewModel = createViewModel { response.await() }
        composeRule.setContent {
            EcoGuardTheme {
                AppealResultRoute(
                    appealId = 2,
                    onBack = {},
                    viewModel = viewModel,
                )
            }
        }

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.common_loading)).assertIsDisplayed()
        response.complete(AppResult.Success(listOf(appeal(2, AppealStatus.Approved))))
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText("승인").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("승인").assertIsDisplayed()
    }

    @Test
    fun retriesHistoryAfterLoadFailure() {
        var requestCount = 0
        val viewModel =
            createViewModel {
                requestCount += 1
                if (requestCount == 1) {
                    AppResult.Failure(AppError.Connectivity)
                } else {
                    AppResult.Success(listOf(appeal(2, AppealStatus.Approved)))
                }
            }
        composeRule.setContent {
            EcoGuardTheme {
                AppealResultRoute(
                    appealId = 2,
                    onBack = {},
                    viewModel = viewModel,
                )
            }
        }

        composeRule.waitUntil(5_000) {
            composeRule
                .onAllNodesWithText(
                    composeRule.activity.getString(R.string.appeal_history_load_failed),
                ).fetchSemanticsNodes()
                .isNotEmpty()
        }
        composeRule.onNodeWithText(composeRule.activity.getString(R.string.action_retry)).performClick()
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText("승인").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.runOnIdle { assertEquals(2, requestCount) }
    }

    @Test
    fun missingAppealIdShowsMissingResult() {
        val viewModel = createViewModel { AppResult.Success(listOf(appeal(2, AppealStatus.Pending))) }
        composeRule.setContent {
            EcoGuardTheme {
                AppealResultRoute(
                    appealId = 99,
                    onBack = {},
                    viewModel = viewModel,
                )
            }
        }

        composeRule.onNodeWithText(composeRule.activity.getString(R.string.appeal_result_missing)).assertIsDisplayed()
    }

    @Test
    fun rendersPendingApprovedAndRejectedServerStatuses() {
        val viewModel =
            createViewModel {
                AppResult.Success(
                    listOf(
                        appeal(1, AppealStatus.Pending),
                        appeal(2, AppealStatus.Approved),
                        appeal(3, AppealStatus.Rejected, replyTitle = "답변 제목", reply = "확인했습니다"),
                    ),
                )
            }
        val selectedAppealId = mutableLongStateOf(1)
        composeRule.setContent {
            EcoGuardTheme {
                AppealResultRoute(
                    appealId = selectedAppealId.longValue,
                    onBack = {},
                    viewModel = viewModel,
                )
            }
        }

        composeRule.onNodeWithText("검수 중").assertIsDisplayed()
        composeRule.runOnIdle { selectedAppealId.longValue = 2 }
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText("승인").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.runOnIdle { selectedAppealId.longValue = 3 }
        composeRule.waitUntil(5_000) {
            composeRule.onAllNodesWithText("확인했습니다").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithText("반려").assertIsDisplayed()
        composeRule.onNodeWithText("선생님 답변").assertIsDisplayed()
        composeRule.onNodeWithText("답변 제목").assertIsDisplayed()
        composeRule.onNodeWithText("확인했습니다").assertIsDisplayed()
    }

    private fun createViewModel(loadAppeals: suspend () -> AppResult<List<MyAppeal>>) =
        AppealHistoryViewModel(
            GetMyAppealsUseCase(
                object : AppealRepository {
                    override suspend fun createAppealWithPhotos(
                        verificationId: Long,
                        content: String,
                        photos: List<java.io.File>,
                    ): AppResult<Unit> = error("unused")

                    override suspend fun createAppeal(
                        verificationId: Long,
                        content: String,
                    ): AppResult<Unit> = error("unused")

                    override suspend fun getMyAppeals(): AppResult<List<MyAppeal>> = loadAppeals()
                },
            ),
        )

    private fun appeal(
        appealId: Long,
        status: AppealStatus,
        replyTitle: String? = null,
        reply: String? = null,
    ) = MyAppeal(
        appealId = appealId,
        verificationId = 10,
        round = 1,
        areaName = "복도",
        verificationDate = "2026-10-25",
        content = "내용",
        photoUrls = emptyList(),
        status = status,
        awardedMinutes = 20,
        replyTitle = replyTitle,
        reply = reply,
        createdAt = "2026-10-25T08:00:00",
    )
}
