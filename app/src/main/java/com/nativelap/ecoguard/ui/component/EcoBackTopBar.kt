package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

/** 뒤로 가기 버튼과 선택적 가운데 제목을 가진 상세 화면 상단 바. */
@Composable
fun EcoBackTopBar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    trailingText: String? = null,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .windowInsetsPadding(topBarWindowInsets),
        contentAlignment = Alignment.TopCenter,
    ) {
        Box(
            modifier =
                Modifier
                    .contentColumnWidth()
                    .heightIn(min = AppComponentSize.backBarHeight)
                    .padding(horizontal = AppSpacing.sm),
            contentAlignment = Alignment.CenterStart,
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(AppComponentSize.minTouchTarget),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = stringResource(R.string.cd_back),
                    modifier = Modifier.size(AppIconSize.standard),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }

            if (title != null) {
                Text(
                    text = title,
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = AppComponentSize.minTouchTarget)
                            .semantics { heading() },
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            if (trailingText != null) {
                Text(
                    text = trailingText,
                    modifier =
                        Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = AppSpacing.xs),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.extraColors.captionTextColor,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun EcoBackTopBarPreview() {
    EcoGuardTheme {
        EcoBackTopBar(
            onBackClick = {},
            title = stringResource(R.string.activity_title),
        )
    }
}
