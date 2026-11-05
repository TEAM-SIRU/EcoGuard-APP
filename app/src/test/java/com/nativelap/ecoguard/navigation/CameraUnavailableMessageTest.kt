package com.nativelap.ecoguard.navigation

import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.feature.home.view.HomePreviewFixtures
import com.nativelap.ecoguard.feature.home.viewmodel.HomeUiState
import com.nativelap.ecoguard.feature.home.viewmodel.TodayCleaningStatus
import com.nativelap.ecoguard.feature.verification.module.domain.model.VerificationUnavailableReason
import org.junit.Assert.assertEquals
import org.junit.Test

class CameraUnavailableMessageTest {
    @Test
    fun serverReasonsProduceAccurateMessages() {
        val reasons =
            mapOf(
                VerificationUnavailableReason.WEEKEND to R.string.verification_weekend_title,
                VerificationUnavailableReason.VACATION to R.string.verification_vacation_title,
                VerificationUnavailableReason.AFTER_END to R.string.verification_time_ended,
                VerificationUnavailableReason.ALREADY_SUBMITTED to R.string.home_already_submitted,
                VerificationUnavailableReason.UNKNOWN to R.string.home_verification_unavailable,
            )
        reasons.forEach { (reason, expected) ->
            val home = HomePreviewFixtures.cleaningHome(TodayCleaningStatus.OutsideVerificationTime("08:00", reason))
            assertEquals(expected, cameraUnavailableMessage(home))
        }
        assertEquals(R.string.home_verification_unavailable, cameraUnavailableMessage(HomeUiState.Loading))
    }
}
