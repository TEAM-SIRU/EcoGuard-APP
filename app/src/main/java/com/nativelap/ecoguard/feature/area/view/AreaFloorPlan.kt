package com.nativelap.ecoguard.feature.area.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.ui.theme.AppRadius

@Composable
fun AreaFloorPlan(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.floor_plan),
        contentDescription = stringResource(R.string.cd_floor_plan),
        modifier = modifier.fillMaxWidth().aspectRatio(342f / 261f).clip(RoundedCornerShape(AppRadius.recordCard)),
        contentScale = ContentScale.Crop,
    )
}
