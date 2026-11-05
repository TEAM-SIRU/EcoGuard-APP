package com.nativelap.ecoguard.navigation

import androidx.lifecycle.ViewModel

/** Keeps successful home-refresh events consumed across Activity recreation. */
internal class HomeRefreshTracker : ViewModel() {
    private var lastConsumedApplicationCount = 0L
    private val consumedVerificationIds = mutableSetOf<Long>()

    fun claimApplicationRefresh(successfulSubmissionCount: Long): Boolean {
        if (successfulSubmissionCount <= lastConsumedApplicationCount) return false
        lastConsumedApplicationCount = successfulSubmissionCount
        return true
    }

    fun claimVerificationRefresh(verificationId: Long): Boolean =
        verificationId > 0L && consumedVerificationIds.add(verificationId)
}
