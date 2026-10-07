package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
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
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/**
 * Scaffold 본문에서 안내 내용을 세로 가운데에 두는 상태·결과 화면 공통 영역.
 * 내용이 화면보다 길면 스크롤된다. Scaffold는 각 Screen이 직접 배치한다.
 */
@Composable
fun CenteredScrollContent(
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    BoxWithConstraints(
        modifier = modifier
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
            content = content,
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CenteredScrollContentPreview() {
    EcoGuardTheme {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surface,
            bottomBar = {
                BottomCtaBar {
                    EcoPrimaryButton(
                        text = stringResource(R.string.action_home),
                        onClick = {},
                    )
                }
            },
        ) { innerPadding ->
            CenteredScrollContent(innerPadding = innerPadding) {
                StatusMessage(
                    title = stringResource(R.string.home_load_failed),
                    description = stringResource(R.string.home_load_failed_description),
                )
            }
        }
    }
}
