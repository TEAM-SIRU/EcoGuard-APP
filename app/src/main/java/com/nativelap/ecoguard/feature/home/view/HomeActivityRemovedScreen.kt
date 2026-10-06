package com.nativelap.ecoguard.feature.home.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.home.viewmodel.HomeScreenEvent
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.StatusMessageLayout
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private const val PARAGRAPH_SEPARATOR = "\n\n"

@Composable
fun HomeActivityRemovedScreen(
    removalReason: String,
    onEvent: (HomeScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val removalDescription = listOf(
        stringResource(R.string.home_removed_from_activity),
        stringResource(R.string.home_removal_reason) + "\n" + removalReason,
        stringResource(R.string.home_contact_teacher_about_reason),
    ).joinToString(separator = PARAGRAPH_SEPARATOR)

    StatusMessageLayout(
        title = stringResource(R.string.home_activity_cancelled),
        description = removalDescription,
        onBackClick = { onEvent(HomeScreenEvent.BackClick) },
        modifier = modifier,
    ) {
        EcoPrimaryButton(
            text = stringResource(R.string.action_home),
            onClick = { onEvent(HomeScreenEvent.HomeClick) },
        )

        EcoSecondaryButton(
            text = stringResource(R.string.action_view_notices),
            onClick = { onEvent(HomeScreenEvent.NoticeListClick) },
        )
    }
}

@Preview(name = "Home · removed from activity", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomeActivityRemovedScreenPreview() {
    EcoGuardTheme {
        HomeActivityRemovedScreen(
            removalReason = "본인 요청으로 활동을 중단했어요.",
            onEvent = {},
        )
    }
}
