package com.nativelap.ecoguard.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

/**
 * 조회 실패·활동 제외처럼 뒤로 가기, 가운데 안내, 하단 버튼으로 구성된 상태 화면의 공통 뼈대.
 * 여러 feature의 상태 화면이 같은 배치를 쓰므로 Scaffold까지 포함한다.
 */
@Composable
fun StatusMessageLayout(
    title: String,
    description: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    highlightText: String? = null,
    bottomActions: @Composable () -> Unit,
) {
    CenteredContentScaffold(
        bottomActions = bottomActions,
        modifier = modifier,
        topBar = {
            EcoBackTopBar(onBackClick = onBackClick)
        },
    ) {
        StatusMessage(
            title = title,
            description = description,
            highlightText = highlightText,
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun StatusMessageLayoutPreview() {
    EcoGuardTheme {
        StatusMessageLayout(
            title = "홈을 불러오지 못했어요",
            description = "네트워크 연결을 확인한 뒤 다시 시도해 주세요.",
            onBackClick = {},
        ) {
            EcoPrimaryButton(
                text = "다시 시도",
                onClick = {},
            )

            EcoSecondaryButton(
                text = "공지 보기",
                onClick = {},
            )
        }
    }
}
