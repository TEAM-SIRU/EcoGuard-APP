package com.nativelap.ecoguard.feature.home.viewmodel

import com.nativelap.ecoguard.feature.home.view.HomePreviewFixtures
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeUiStateTest {
    @Test
    fun cameraVerificationIsAvailableWhenAreaIsAssigned() {
        assertTrue(HomePreviewFixtures.notSubmittedHome.isCameraVerificationAvailable)
    }

    @Test
    fun cameraVerificationIsUnavailableWhileRecruiting() {
        assertFalse(HomePreviewFixtures.recruitingHome.isCameraVerificationAvailable)
    }

    @Test
    fun cameraVerificationIsUnavailableWhileWaitingForAssignment() {
        assertFalse(HomePreviewFixtures.waitingAssignmentHome.isCameraVerificationAvailable)
    }
}
