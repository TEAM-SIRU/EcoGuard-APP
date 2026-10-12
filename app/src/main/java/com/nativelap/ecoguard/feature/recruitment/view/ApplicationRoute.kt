package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.ui.component.TextInputLength

@Composable
fun ApplicationRoute(
    uiState: ApplicationUiState,
    onEvent: (RecruitmentScreenEvent) -> Unit = {},
) {
    var motivation by rememberSaveable(uiState.grade, uiState.classNumber, uiState.studentNumber, uiState.motivation) {
        mutableStateOf(TextInputLength.limit(uiState.motivation, ApplicationUiState.MAX_MOTIVATION_LENGTH))
    }
    val applicationState = uiState.copy(motivation = motivation)
    ApplicationScreen(
        uiState = applicationState,
        onEvent = { screenEvent ->
            when (screenEvent) {
                is RecruitmentScreenEvent.MotivationChange -> {
                    if (!uiState.isApplying) {
                        motivation =
                            TextInputLength.limit(screenEvent.motivation, ApplicationUiState.MAX_MOTIVATION_LENGTH)
                        onEvent(screenEvent.copy(motivation = motivation))
                    }
                }

                RecruitmentScreenEvent.ApplyClick -> {
                    if (applicationState.canApply) onEvent(screenEvent)
                }

                else -> {
                    onEvent(screenEvent)
                }
            }
        },
    )
}
