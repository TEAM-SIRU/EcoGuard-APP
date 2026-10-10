package com.nativelap.ecoguard.feature.area.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.area.viewmodel.AreaUiState
import com.nativelap.ecoguard.ui.component.EcoCard
import com.nativelap.ecoguard.ui.component.TwoLineTextRow
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing

@Composable
fun AreaAssignmentCard(
    areaContent: AreaUiState.Content,
    memberNames: String,
    modifier: Modifier = Modifier,
) {
    EcoCard(
        modifier = modifier,
        cornerRadius = AppRadius.listCard,
        contentPadding = PaddingValues(vertical = AppSpacing.xxs),
    ) {
        TwoLineTextRow(
            title = areaContent.areaName,
            subtitle = stringResource(R.string.area_description_format, areaContent.areaDescription),
        )

        TwoLineTextRow(
            title =
                stringResource(
                    R.string.recruitment_daily_time,
                    areaContent.cleaningStartTime,
                    areaContent.cleaningEndTime,
                ),
            subtitle = stringResource(R.string.home_cleaning_time),
        )

        TwoLineTextRow(
            title = stringResource(R.string.area_member_count, areaContent.teammateNames.size + 1),
            subtitle = memberNames,
        )
    }
}
