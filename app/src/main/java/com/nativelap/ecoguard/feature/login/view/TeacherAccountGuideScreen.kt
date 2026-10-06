package com.nativelap.ecoguard.feature.login.view

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.login.viewmodel.TeacherAccountGuideScreenEvent
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredIconMessage
import com.nativelap.ecoguard.ui.component.EcoSecondaryButton
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val teacherGuideButtonSpacing = 10.dp

@Composable
fun TeacherAccountGuideScreen(
    onEvent: (TeacherAccountGuideScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            BottomCtaBar(itemSpacing = teacherGuideButtonSpacing) {
                EcoSecondaryButton(
                    text = stringResource(R.string.web_address_copy),
                    onClick = { onEvent(TeacherAccountGuideScreenEvent.CopyWebAddressClick) },
                )

                EcoSecondaryButton(
                    text = stringResource(R.string.web_address_share),
                    onClick = { onEvent(TeacherAccountGuideScreenEvent.ShareWebAddressClick) },
                )

                EcoSecondaryButton(
                    text = stringResource(R.string.action_logout),
                    onClick = { onEvent(TeacherAccountGuideScreenEvent.LogoutClick) },
                )
            }
        },
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = AppComponentSize.contentMaxWidth)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = maxHeight)
                    .padding(
                        horizontal = AppSpacing.screenHorizontal,
                        vertical = AppSpacing.xl,
                    ),
                verticalArrangement = Arrangement.Center,
            ) {
                CenteredIconMessage(
                    iconRes = R.drawable.ic_list_30,
                    title = stringResource(R.string.teacher_web_only_title),
                    description = stringResource(R.string.teacher_web_only_description),
                )
            }
        }
    }
}

@Preview(name = "Teacher guide", showBackground = true, widthDp = 390, heightDp = 844)
@Preview(name = "Teacher guide · compact larger text", showBackground = true, widthDp = 320, heightDp = 596, fontScale = 1.5f)
@Composable
private fun TeacherAccountGuideScreenPreview() {
    EcoGuardTheme {
        TeacherAccountGuideScreen(onEvent = {})
    }
}
