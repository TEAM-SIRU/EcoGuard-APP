/*
 * Based on AndroidX Navigation 3 AnimatedBottomSheetSample.
 * Copyright 2026 The Android Open Source Project
 * Licensed under the Apache License, Version 2.0 (https://www.apache.org/licenses/LICENSE-2.0).
 */
package com.nativelap.ecoguard.navigation

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.NavMetadataKey
import androidx.navigation3.runtime.get
import androidx.navigation3.runtime.metadata
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import com.nativelap.ecoguard.ui.theme.AppComponentSize
import com.nativelap.ecoguard.ui.theme.AppRadius

@OptIn(ExperimentalMaterial3Api::class)
class EcoGuardBottomSheetSceneStrategy : SceneStrategy<NavKey> {
    override fun SceneStrategyScope<NavKey>.calculateScene(entries: List<NavEntry<NavKey>>): Scene<NavKey>? {
        val topEntry = entries.lastOrNull() ?: return null
        if (topEntry.metadata[SheetKey] != true || entries.size < 2) return null
        return EcoGuardSheetScene(topEntry, entries.dropLast(1), onBack)
    }

    companion object {
        private object SheetKey : NavMetadataKey<Boolean>

        fun bottomSheet(): Map<String, Any> = metadata { put(SheetKey, true) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private class EcoGuardSheetScene(
    val entry: NavEntry<NavKey>,
    override val previousEntries: List<NavEntry<NavKey>>,
    val onBack: () -> Unit,
) : OverlayScene<NavKey> {
    override val key = entry.contentKey
    override val entries = listOf(entry)
    override val overlaidEntries = previousEntries
    private var sheetState: SheetState? = null
    override val content: @Composable () -> Unit = {
        val modalState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        sheetState = modalState
        ModalBottomSheet(
            onDismissRequest = onBack,
            sheetState = modalState,
            sheetMaxWidth = AppComponentSize.contentMaxWidth,
            shape = RoundedCornerShape(topStart = AppRadius.logo, topEnd = AppRadius.logo),
            containerColor = MaterialTheme.colorScheme.surface,
            scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.4f),
        ) { entry.Content() }
    }

    override suspend fun onRemove() {
        sheetState?.hide()
    }

    override fun equals(other: Any?): Boolean =
        other is EcoGuardSheetScene && entry == other.entry && previousEntries == other.previousEntries

    override fun hashCode(): Int = 31 * entry.hashCode() + previousEntries.hashCode()
}
