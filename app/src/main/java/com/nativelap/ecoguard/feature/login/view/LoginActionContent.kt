package com.nativelap.ecoguard.feature.login.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors
import com.nativelap.ecoguard.ui.theme.extraTypography

private val loginActionSpacing = 10.dp

/** 로그인 버튼(기본·로딩·재시도 문구)과 학교 계정 안내 문구. */
@Composable
fun LoginActionContent(
    isLoggingIn: Boolean,
    hasLoginFailed: Boolean,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val buttonTextRes = when {
        isLoggingIn -> R.string.login_in_progress
        hasLoginFailed -> R.string.action_retry_login
        else -> R.string.login_with_datagsm
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(loginActionSpacing),
    ) {
        EcoPrimaryButton(
            text = stringResource(buttonTextRes),
            onClick = onLoginClick,
            isLoading = isLoggingIn,
        )

        Text(
            text = stringResource(R.string.login_school_account_only),
            style = MaterialTheme.extraTypography.captionRegular,
            color = MaterialTheme.extraColors.captionTextColor,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun LoginActionContentPreview() {
    EcoGuardTheme {
        LoginActionContent(
            isLoggingIn = false,
            hasLoginFailed = false,
            onLoginClick = {},
        )
    }
}
