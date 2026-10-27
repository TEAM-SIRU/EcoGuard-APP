package com.nativelap.ecoguard.feature.area.view

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
        if (areaContent.areaDescription != null) {
            TwoLineTextRow(
                title = areaContent.areaName,
                subtitle = stringResource(R.string.area_description_format, areaContent.areaDescription),
                verticalPadding = 12.dp,
            )
        } else {
            TwoLineTextRow(
                title = areaContent.areaName,
                subtitle = "",
                verticalPadding = 12.dp,
            )
        }

        if (areaContent.cleaningTime != null) {
            TwoLineTextRow(
                title = areaContent.cleaningTime,
                subtitle = stringResource(R.string.home_cleaning_time),
                verticalPadding = 12.dp,
            )
        }

        TwoLineTextRow(
            title = stringResource(R.string.area_member_count, areaContent.memberNames.size),
            subtitle = memberNames,
            verticalPadding = 12.dp,
        )
    }
}
