package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppComponentSize

/** 가로 배치 행(라벨·값, 제목·칩)이 기본 글자 크기 기준으로 필요로 하는 최소 폭. */
object AdaptiveWidth {
    val infoRow = 220.dp
    val trailingRow = 240.dp
    val buttonRow = 200.dp
    val statItem = 72.dp
    val noticeHeader = 280.dp
}

/**
 * 사용할 수 있는 폭이 글자 크기를 반영했을 때 기준 폭보다 좁은지 판단한다.
 * 글자 배율만큼 폭을 나눠 비교하므로, 폭이 좁은 기기와 글자 확대를 같은 기준으로 처리한다.
 */
@Composable
@ReadOnlyComposable
fun isCompactForText(
    availableWidth: Dp,
    requiredWidth: Dp,
): Boolean {
    return availableWidth / LocalDensity.current.fontScale < requiredWidth
}

/** 넓은 화면에서도 화면 콘텐츠와 같은 최대 폭(600dp) 안에 배치한다. 바깥 컨테이너는 가운데 정렬해야 한다. */
fun Modifier.contentColumnWidth(): Modifier {
    return widthIn(max = AppComponentSize.contentMaxWidth).fillMaxWidth()
}

/** 상단 바가 피해야 하는 영역: 상태 바와 가로 방향 디스플레이 컷아웃. */
val topBarWindowInsets: WindowInsets
    @Composable
    get() = WindowInsets.statusBars.union(WindowInsets.displayCutout.only(WindowInsetsSides.Horizontal))
