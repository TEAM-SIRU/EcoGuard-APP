package com.nativelap.ecoguard.ui.component

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdaptiveLayoutTest {
    @Test
    fun navigationRailStartsAtExpandedLayoutBreakpoint() {
        assertFalse(isExpandedLayout(839.dp))
        assertTrue(isExpandedLayout(840.dp))
    }
}
