package com.nativelap.ecoguard.feature.menu.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

/** 전체 메뉴 하단의 로그아웃·회원탈퇴처럼 회색 글자로 표시하는 계정 동작. */
@Composable
fun MenuTextAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier =
            modifier
                .fillMaxWidth()
                .heightIn(min = AppComponentSize.minTouchTarget)
                .clickable(
                    role = Role.Button,
                    onClick = onClick,
                ).padding(
                    horizontal = AppSpacing.screenHorizontal,
                    vertical = AppSpacing.md,
                ),
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.extraColors.captionTextColor,
    )
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun MenuTextActionPreview() {
    EcoGuardTheme {
        MenuTextAction(
            text = stringResource(R.string.action_logout),
            onClick = {},
        )
    }
}
