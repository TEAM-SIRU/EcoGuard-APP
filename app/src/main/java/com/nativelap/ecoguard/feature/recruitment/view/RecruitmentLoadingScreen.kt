package com.nativelap.ecoguard.feature.recruitment.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.component.LoadingSkeletonScrollContent
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 03 모집 공고 로딩. 회색 제목과 스켈레톤을 표시한다. */
@Composable
fun RecruitmentLoadingScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        LoadingSkeletonScrollContent(
            innerPadding = innerPadding,
            title = stringResource(R.string.recruitment_notice_title),
        )
    }
}

@Preview(name = "Recruitment · loading", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun RecruitmentLoadingScreenPreview() {
    EcoGuardTheme {
        RecruitmentLoadingScreen()
    }
}
