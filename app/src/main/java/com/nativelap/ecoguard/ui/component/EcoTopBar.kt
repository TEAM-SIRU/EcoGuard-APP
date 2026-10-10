package com.nativelap.ecoguard.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/** 탭 화면 상단의 큰 제목과 오른쪽 액션 아이콘(기본 알림). */
@Composable
fun EcoTopBar(
    title: String,
    modifier: Modifier = Modifier,
    @DrawableRes actionIconRes: Int? = R.drawable.ic_bell,
    actionContentDescription: String? = stringResource(R.string.cd_notice),
    onActionClick: () -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .windowInsetsPadding(topBarWindowInsets),
        contentAlignment = Alignment.TopCenter,
    ) {
        Row(
            modifier =
                Modifier
                    .contentColumnWidth(twoPaneContentMaxWidth())
                    .heightIn(min = AppComponentSize.topBarHeight)
                    .padding(
                        start = AppSpacing.screenHorizontal,
                        end = AppSpacing.screenHorizontal - AppSpacing.xxs,
                    ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                modifier =
                    Modifier
                        .weight(1f)
                        .semantics { heading() },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (actionIconRes != null) {
                IconButton(
                    onClick = onActionClick,
                    modifier = Modifier.size(AppComponentSize.minTouchTarget),
                ) {
                    Icon(
                        painter = painterResource(actionIconRes),
                        contentDescription = actionContentDescription,
                        modifier = Modifier.size(AppIconSize.standard),
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun EcoTopBarPreview() {
    EcoGuardTheme {
        EcoTopBar(title = stringResource(R.string.brand_name))
    }
}
