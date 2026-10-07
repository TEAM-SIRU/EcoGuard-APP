package com.nativelap.ecoguard.feature.home.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.home.viewmodel.HomeNoticeUiModel
import com.nativelap.ecoguard.ui.component.AdaptiveWidth
import com.nativelap.ecoguard.ui.component.EcoCard
import com.nativelap.ecoguard.ui.component.isCompactForText
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppIconSize
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors
import com.nativelap.ecoguard.ui.theme.extraTypography

private val noticeTextSpacing = 2.dp
private val noticeBadgeHorizontalPadding = 10.dp
private val noticeBadgeVerticalPadding = 3.dp
private val noticeCardBottomPadding = 18.dp
private val noticeLinkSpacing = 2.dp

@Composable
fun HomeNoticeCard(
    notice: HomeNoticeUiModel,
    onCloseClick: () -> Unit,
    onDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    EcoCard(
        modifier = modifier,
        emphasized = true,
        contentPadding = PaddingValues(
            start = AppSpacing.lg,
            end = AppSpacing.lg,
            top = AppSpacing.lg,
            bottom = noticeCardBottomPadding,
        ),
    ) {
        BoxWithConstraints {
            // 좁은 폭·큰 글자에서는 제목이 세로로 꺾이지 않도록 NEW 배지를 날짜 아래로 내린다.
            val isCompactHeader = isCompactForText(maxWidth, AdaptiveWidth.noticeHeader)

            Row(
                horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(AppComponentSize.noticeIconFrame)
                        .clip(RoundedCornerShape(AppRadius.iconFrame)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_megaphone),
                        contentDescription = null,
                        modifier = Modifier.size(AppIconSize.standard),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(noticeTextSpacing),
                ) {
                    Text(
                        text = stringResource(R.string.home_notice_title),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Text(
                        text = notice.publishedDate,
                        style = MaterialTheme.extraTypography.captionRegular,
                        color = MaterialTheme.extraColors.captionTextColor,
                    )

                    if (notice.isNew && isCompactHeader) {
                        NoticeNewBadge()
                    }
                }

                if (notice.isNew && !isCompactHeader) {
                    NoticeNewBadge()
                }

                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier.size(AppComponentSize.minTouchTarget),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.cd_close),
                        modifier = Modifier.size(AppIconSize.small),
                        tint = MaterialTheme.extraColors.captionTextColor,
                    )
                }
            }
        }

        Text(
            text = notice.headline,
            modifier = Modifier.padding(
                top = AppSpacing.md,
                bottom = AppSpacing.xs,
            ),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            text = notice.body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Row(
            modifier = Modifier
                .padding(top = AppSpacing.xs)
                .heightIn(min = AppComponentSize.minTouchTarget)
                .clickable(
                    role = Role.Button,
                    onClick = onDetailClick,
                ),
            horizontalArrangement = Arrangement.spacedBy(noticeLinkSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.action_view_details),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
            )

            Icon(
                painter = painterResource(R.drawable.ic_chevron_right),
                contentDescription = null,
                modifier = Modifier.size(AppIconSize.link),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun NoticeNewBadge() {
    Text(
        text = stringResource(R.string.badge_new),
        modifier = Modifier
            .clip(RoundedCornerShape(AppRadius.badge))
            .background(MaterialTheme.colorScheme.primary)
            .padding(
                horizontal = noticeBadgeHorizontalPadding,
                vertical = noticeBadgeVerticalPadding,
            ),
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onPrimary,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF8FAF9, widthDp = 390)
@Preview(name = "Compact larger text", showBackground = true, backgroundColor = 0xFFF8FAF9, widthDp = 320, fontScale = 1.5f)
@Composable
private fun HomeNoticeCardPreview() {
    EcoGuardTheme {
        HomeNoticeCard(
            notice = HomePreviewFixtures.notice,
            onCloseClick = {},
            onDetailClick = {},
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
