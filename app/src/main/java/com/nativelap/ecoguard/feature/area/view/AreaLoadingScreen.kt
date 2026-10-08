package com.nativelap.ecoguard.feature.area.view

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

/** 05 청소구역 로딩. 회색 제목과 스켈레톤을 표시한다. */
@Composable
fun AreaLoadingScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
    ) { innerPadding ->
        LoadingSkeletonScrollContent(
            innerPadding = innerPadding,
            title = stringResource(R.string.common_my_cleaning_area),
        )
    }
}

@Preview(name = "Area · loading", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AreaLoadingScreenPreview() {
    EcoGuardTheme {
        AreaLoadingScreen()
    }
}
