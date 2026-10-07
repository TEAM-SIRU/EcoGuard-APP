package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.recruitment.viewmodel.RecruitmentScreenEvent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val applicationTitleSpacing = 6.dp
private val applicationActionTopPadding = 24.dp

/** 환경지킴이 신청 확인 화면. 신청 요청 중에는 버튼을 비활성화한다. */
@Composable
fun ApplicationScreen(
    maxApplicantsPerClass: Int,
    isApplying: Boolean,
    onEvent: (RecruitmentScreenEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            EcoBackTopBar(onBackClick = { onEvent(RecruitmentScreenEvent.BackClick) })
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
                Column(verticalArrangement = Arrangement.spacedBy(applicationTitleSpacing)) {
                    Text(
                        text = stringResource(R.string.application_title),
                        modifier = Modifier.semantics { heading() },
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = stringResource(R.string.application_description, maxApplicantsPerClass),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                EcoPrimaryButton(
                    text = stringResource(R.string.recruitment_apply),
                    onClick = { onEvent(RecruitmentScreenEvent.ApplyClick) },
                    modifier = Modifier.padding(top = applicationActionTopPadding),
                    enabled = !isApplying,
                )
            }
        }
    }
}

@Preview(name = "Application · ready", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ApplicationScreenPreview() {
    EcoGuardTheme {
        ApplicationScreen(
            maxApplicantsPerClass = 6,
            isApplying = false,
            onEvent = {},
        )
    }
}

@Preview(name = "Application · applying (Figma)", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun ApplicationScreenApplyingPreview() {
    EcoGuardTheme {
        ApplicationScreen(
            maxApplicantsPerClass = 6,
            isApplying = true,
            onEvent = {},
        )
    }
}
