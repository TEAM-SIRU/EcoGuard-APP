package com.nativelap.ecoguard.ui.theme

import androidx.compose.ui.unit.dp

object AppComponentSize {
    val primaryButtonHeight = 50.dp
    val dialogButtonHeight = 48.dp
    val toastMinHeight = 50.dp
    val topBarHeight = 52.dp
    val backBarHeight = 48.dp
    val minTouchTarget = 48.dp
    val iconTile = 40.dp
    val noticeIconFrame = 40.dp
    // 탭 항목 영역 높이. 시스템 바 여백(6dp)과 내비게이션 바 inset은 별도로 더해진다.
    val tabBarHeight = 54.dp
    val cameraFab = 54.dp
    val cameraFabBorder = 4.dp

    // 카메라 버튼이 탭 바 위로 튀어나온 높이
    val cameraFabProtrusion = 16.dp

    // 탭 바 위로 튀어나온 카메라 버튼과 그림자에 마지막 항목이 가리지 않도록 스크롤 끝에 두는 여백.
    // 12dp = FAB 그림자가 위로 번지는 폭(반경 14 − 아래 이동 6 = 8) + 여유 4. 그림자 값을 바꾸면 함께 조정한다.
    val cameraFabScrollClearance = cameraFabProtrusion + 12.dp
    val weekDayIndicator = 36.dp
    val progressBarHeight = 6.dp
    val logo = 72.dp
    val emptyIconCircle = 56.dp
    val contentMaxWidth = 600.dp

    // 넓은 화면(폭 840dp 이상)에서 카드형 화면을 2열로 배치할 때의 최대 폭
    val expandedContentMaxWidth = 960.dp
    val expandedLayoutMinWidth = 840.dp
}
