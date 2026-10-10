package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.recruitment.viewmodel.ApplicationUiState
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.ui.component.EcoTextArea
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.extraColors

@Composable
fun ApplicationFormContent(
    uiState: ApplicationUiState,
    onEvent: (RecruitmentScreenEvent) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xl)) {
        ApplicationIdentityField(
            label = stringResource(R.string.application_student_number),
            text =
                stringResource(
                    R.string.application_student_identity,
                    uiState.grade,
                    uiState.classNumber,
                    uiState.studentNumber,
                ),
        )
        ApplicationIdentityField(
            label = stringResource(R.string.application_student_name),
            text = uiState.studentName,
            helper = stringResource(R.string.application_identity_helper),
        )
        Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xs)) {
            Text(
                stringResource(R.string.application_motivation),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.extraColors.captionTextColor,
            )
            EcoTextArea(
                value = uiState.motivation,
                onValueChange = { onEvent(RecruitmentScreenEvent.MotivationChange(it)) },
                placeholder = stringResource(R.string.application_motivation_placeholder),
                maxLength = ApplicationUiState.MAX_MOTIVATION_LENGTH,
                label = stringResource(R.string.application_motivation),
                enabled = !uiState.isApplying,
                emphasizeFilled = true,
            )
        }
    }
}
