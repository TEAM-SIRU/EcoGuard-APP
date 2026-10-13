package com.nativelap.ecoguard.feature.demo.viewmodel

import androidx.compose.runtime.Immutable

@Immutable
data class DemoAppealDraft(
    val content: String = "",
    val photoCount: Int = 0,
)
