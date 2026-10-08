package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationDayOffReason
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationScreenEvent
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredIconMessage
import com.nativelap.ecoguard.ui.component.CenteredIconStyle
import com.nativelap.ecoguard.ui.component.CenteredScrollContent
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 주말·방학이라 청소 인증을 할 수 없음을 안내하고 홈으로 돌려보낸다. */
@Composable
fun VerificationDayOffScreen(
    dayOffReason: VerificationDayOffReason,
    onEvent: (VerificationScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val titleRes =
        when (dayOffReason) {
            VerificationDayOffReason.WEEKEND -> R.string.verification_weekend_title
            VerificationDayOffReason.SCHOOL_VACATION -> R.string.verification_vacation_title
        }
    val descriptionRes =
        when (dayOffReason) {
            VerificationDayOffReason.WEEKEND -> R.string.verification_weekend_description
            VerificationDayOffReason.SCHOOL_VACATION -> R.string.verification_vacation_description
        }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            BottomCtaBar {
                EcoPrimaryButton(
                    text = stringResource(R.string.action_home),
                    onClick = { onEvent(VerificationScreenEvent.HomeClick) },
                )
            }
        },
    ) { innerPadding ->
        CenteredScrollContent(innerPadding = innerPadding) {
            CenteredIconMessage(
                iconRes = R.drawable.ic_clock_64,
                title = stringResource(titleRes),
                description = stringResource(descriptionRes),
                iconStyle = CenteredIconStyle.HERO,
            )
        }
    }
}

@Preview(name = "Day off · weekend", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun VerificationDayOffWeekendPreview() {
    EcoGuardTheme {
        VerificationDayOffScreen(
            dayOffReason = VerificationDayOffReason.WEEKEND,
            onEvent = {},
        )
    }
}

@Preview(name = "Day off · school vacation", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(name = "Day off · compact larger text", showBackground = true, widthDp = 320, heightDp = 596, fontScale = 1.5f)
@Composable
private fun VerificationDayOffVacationPreview() {
    EcoGuardTheme {
        VerificationDayOffScreen(
            dayOffReason = VerificationDayOffReason.SCHOOL_VACATION,
            onEvent = {},
        )
    }
}
