package com.nativelap.ecoguard.feature.home.view

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.ThemeMode
import com.nativelap.ecoguard.ui.theme.extraTypography

@Composable
fun HomeLoadingStatus(
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.common_loading),
        modifier = modifier.semantics {
            liveRegion = LiveRegionMode.Polite
        },
        style = MaterialTheme.extraTypography.loadingStatus,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Preview(showBackground = true)
@Composable
private fun HomeLoadingStatusPreview() {
    EcoGuardTheme(themeMode = ThemeMode.LIGHT) {
        HomeLoadingStatus()
    }
}
