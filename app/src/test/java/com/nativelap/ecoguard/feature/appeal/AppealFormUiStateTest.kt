package com.nativelap.ecoguard.feature.appeal

import com.nativelap.ecoguard.feature.appeal.viewmodel.AppealFormUiState
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AppealFormUiStateTest {
    @Test
    fun canSubmitOnlyWhenContentExistsAndSubmissionIsNotInProgressOrComplete() {
        val validContent = AppealFormUiState("reason", "appeal", 0, 3, 500, "17:00")

        assertTrue(validContent.canSubmit)
        assertFalse(validContent.copy(appealContent = "").canSubmit)
        assertFalse(validContent.copy(isSubmitting = true).canSubmit)
        assertFalse(validContent.copy(isSubmitted = true).canSubmit)
    }
}
