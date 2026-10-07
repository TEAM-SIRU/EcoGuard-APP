package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.AppSpacing
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme
import com.nativelap.ecoguard.ui.theme.extraColors

private val photoIconSize = 36.dp

/** 사진 자리 표시. 실제 사진은 촬영·서버 연동 작업에서 이 영역에 표시한다. */
@Composable
fun PhotoPlaceholder(
    label: String,
    height: Dp,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(AppRadius.card))
            .background(MaterialTheme.colorScheme.surfaceVariant),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = AppSpacing.xs,
            alignment = Alignment.CenterVertically,
        ),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_image),
            contentDescription = null,
            modifier = Modifier.size(photoIconSize),
            tint = MaterialTheme.extraColors.captionTextColor,
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.extraColors.captionTextColor,
        )
    }
}

@Preview(showBackground = true, widthDp = 390)
@Composable
private fun PhotoPlaceholderPreview() {
    EcoGuardTheme {
        PhotoPlaceholder(
            label = "촬영한 사진",
            height = 256.dp,
            modifier = Modifier.padding(AppSpacing.xl),
        )
    }
}
