package com.nativelap.ecoguard.feature.home.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.component.LoadingSkeletonLayout
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun HomeLoadingScreen(
    modifier: Modifier = Modifier,
) {
    LoadingSkeletonLayout(
        title = stringResource(R.string.brand_name),
        modifier = modifier,
    )
}

@Preview(name = "Home · loading", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun HomeLoadingScreenPreview() {
    EcoGuardTheme {
        HomeLoadingScreen()
    }
}
