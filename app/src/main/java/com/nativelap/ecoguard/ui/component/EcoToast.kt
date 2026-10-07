package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

private val toastIconSpacing = 10.dp
private val toastVerticalPadding = 14.dp

/** 짙은 배경의 오류 안내 토스트. 아이콘은 Figma 원본 색(연한 빨강)을 유지한다. */
@Composable
fun EcoToast(
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = AppComponentSize.toastMinHeight)
            .clip(RoundedCornerShape(AppRadius.toast))
            .background(MaterialTheme.colorScheme.inverseSurface)
            .padding(
                horizontal = AppSpacing.md,
                vertical = toastVerticalPadding,
            )
            .semantics { liveRegion = LiveRegionMode.Polite },
        horizontalArrangement = Arrangement.spacedBy(toastIconSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_alert_20),
            contentDescription = null,
            modifier = Modifier.size(AppIconSize.button),
            tint = Color.Unspecified,
        )

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.inverseOnSurface,
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun EcoToastPreview() {
    EcoGuardTheme {
        EcoToast(
            message = "로그인하지 못했어요. 다시 시도해 주세요",
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
