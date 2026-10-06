package com.nativelap.ecoguard.feature.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.component.EcoCard
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.EcoTag
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val applicationCardTopPadding = 24.dp
private val applicationTitleTopPadding = 10.dp
private val applicationTextSpacing = 6.dp

/** 신청 완료 후 청소 구역 배정을 기다리는 동안 보여주는 카드. */
@Composable
fun HomeApplicationCompletedCard(
    onApplicationResultClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EcoCard(
        modifier = modifier,
        contentPadding = PaddingValues(
            start = AppSpacing.lg,
            end = AppSpacing.lg,
            top = applicationCardTopPadding,
            bottom = AppSpacing.lg,
        ),
    ) {
        EcoTag(text = stringResource(R.string.home_application_completed))

        Column(
            modifier = Modifier.padding(
                top = applicationTitleTopPadding,
                bottom = AppSpacing.md,
            ),
            verticalArrangement = Arrangement.spacedBy(applicationTextSpacing),
        ) {
            Text(
                text = stringResource(R.string.home_became_guardian),
                modifier = Modifier.semantics { heading() },
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = stringResource(R.string.home_assignment_notice),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        EcoSecondaryButton(
            text = stringResource(R.string.home_see_application_result),
            onClick = onApplicationResultClick,
            modifier = Modifier.padding(top = AppSpacing.lg),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAF9, widthDp = 390)
@Composable
private fun HomeApplicationCompletedCardPreview() {
    EcoGuardTheme {
        HomeApplicationCompletedCard(
            onApplicationResultClick = {},
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
