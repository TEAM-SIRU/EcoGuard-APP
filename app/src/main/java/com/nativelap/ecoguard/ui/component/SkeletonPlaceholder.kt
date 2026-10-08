package com.nativelap.ecoguard.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nativelap.ecoguard.ui.theme.AppRadius
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun SkeletonPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .clearAndSetSemantics { }
                .clip(RoundedCornerShape(AppRadius.tile))
                .background(MaterialTheme.colorScheme.surfaceVariant),
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 240)
@Composable
private fun SkeletonPlaceholderPreview() {
    EcoGuardTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
        ) {
            SkeletonPlaceholder(
                modifier =
                    Modifier
                        .padding(24.dp)
                        .size(width = 342.dp, height = 156.dp),
            )
        }
    }
}
