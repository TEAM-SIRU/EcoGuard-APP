package com.nativelap.ecoguard.feature.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.home.viewmodel.HomeSectionUiModel
import com.nativelap.ecoguard.ui.component.EcoCard
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoProgressBar
import com.nativelap.ecoguard.ui.component.EcoTag
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val recruitmentCardTopPadding = 20.dp
private val recruitmentTitleTopPadding = 8.dp
private val recruitmentTextSpacing = 6.dp

/** 모집 기간에 아직 신청하지 않은 학생에게 보여주는 모집 카드. */
@Composable
fun HomeRecruitmentCard(
    recruiting: HomeSectionUiModel.Recruiting,
    onRecruitmentClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EcoCard(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = AppSpacing.lg,
            end = AppSpacing.lg,
            top = recruitmentCardTopPadding,
            bottom = AppSpacing.lg,
        ),
    ) {
        EcoTag(text = stringResource(R.string.home_recruiting))

        Column(
            modifier = Modifier.padding(
                top = recruitmentTitleTopPadding,
                bottom = AppSpacing.md,
            ),
            verticalArrangement = Arrangement.spacedBy(recruitmentTextSpacing),
        ) {
            Text(
                text = stringResource(R.string.home_recruiting_title, recruiting.semesterName),
                modifier = Modifier.semantics { heading() },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = stringResource(R.string.home_recruiting_policy, recruiting.maxApplicantsPerClass),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(
                    R.string.format_grade_class,
                    recruiting.grade,
                    recruiting.classNumber,
                ),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text(
                text = stringResource(
                    R.string.format_application_headcount,
                    recruiting.appliedCount,
                    recruiting.maxApplicantsPerClass,
                ),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
            )
        }

        EcoProgressBar(
            currentCount = recruiting.appliedCount,
            maxCount = recruiting.maxApplicantsPerClass,
        )

        EcoPrimaryButton(
            text = stringResource(R.string.home_see_recruitment),
            onClick = onRecruitmentClick,
            modifier = Modifier.padding(top = AppSpacing.lg),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAF9, widthDp = 390)
@Composable
private fun HomeRecruitmentCardPreview() {
    EcoGuardTheme {
        HomeRecruitmentCard(
            recruiting = HomePreviewFixtures.recruiting,
            onRecruitmentClick = {},
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
