package com.nativelap.ecoguard.feature.menu.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.menu.viewmodel.MenuUiState
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val profileAvatarSize = 48.dp
private val profileSpacing = 12.dp
private val profileTextSpacing = 2.dp
private val profileInitialsMinFontSize = 10.sp

@Composable
fun MenuProfileHeader(
    menuState: MenuUiState,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(profileSpacing),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(profileAvatarSize)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .clearAndSetSemantics { },
            contentAlignment = Alignment.Center,
        ) {
            // 원 크기는 고정이므로 큰 글자에서는 이니셜을 원 안에 맞게 줄인다.
            BasicText(
                text = menuState.profileInitials,
                modifier = Modifier.padding(AppSpacing.xxs),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                maxLines = 1,
                autoSize = TextAutoSize.StepBased(
                    minFontSize = profileInitialsMinFontSize / LocalDensity.current.fontScale,
                    maxFontSize = MaterialTheme.typography.titleMedium.fontSize,
                ),
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(profileTextSpacing),
        ) {
            Text(
                text = menuState.studentName,
                modifier = Modifier.semantics { heading() },
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = stringResource(R.string.profile_class_role, menuState.grade, menuState.classNumber),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.extraColors.captionTextColor,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun MenuProfileHeaderPreview() {
    EcoGuardTheme {
        MenuProfileHeader(
            menuState = MenuPreviewFixtures.menuState,
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
