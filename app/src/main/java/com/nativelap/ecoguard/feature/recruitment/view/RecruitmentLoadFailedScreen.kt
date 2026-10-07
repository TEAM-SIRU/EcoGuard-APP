package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.component.StatusMessageLayout
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun RecruitmentLoadFailedScreen(
    onEvent: (RecruitmentScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    StatusMessageLayout(
        title = stringResource(R.string.recruitment_load_failed),
        description = stringResource(R.string.recruitment_load_failed_description),
        onBackClick = { onEvent(RecruitmentScreenEvent.BackClick) },
        modifier = modifier,
    ) {
        EcoPrimaryButton(
            text = stringResource(R.string.action_retry),
            onClick = { onEvent(RecruitmentScreenEvent.RetryClick) },
        )

        EcoSecondaryButton(
            text = stringResource(R.string.action_home),
            onClick = { onEvent(RecruitmentScreenEvent.HomeClick) },
        )
    }
}

@Preview(name = "Recruitment · load failed", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun RecruitmentLoadFailedScreenPreview() {
    EcoGuardTheme {
        RecruitmentLoadFailedScreen(onEvent = {})
    }
}
