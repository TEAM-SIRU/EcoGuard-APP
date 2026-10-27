package com.nativelap.ecoguard.core.presentation

import androidx.lifecycle.ViewModel
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Test

class FeatureViewModelStoreHolderTest {
    @Test
    fun `each session receives an isolated view model store`() {
        val holder = FeatureViewModelStoreHolder()

        val firstSessionStore = holder.storeFor("session-a")
        val secondSessionStore = holder.storeFor("session-b")

        assertSame(firstSessionStore, holder.storeFor("session-a"))
        assertNotSame(firstSessionStore, secondSessionStore)
    }

    @Test
    fun `clearing a session removes only its view models`() {
        val holder = FeatureViewModelStoreHolder()
        val firstSessionStore = holder.storeFor("session-a")
        val secondSessionStore = holder.storeFor("session-b")
        firstSessionStore.put("profile", TestViewModel())
        secondSessionStore.put("profile", TestViewModel())

        holder.clearSession("session-a")

        assertNull(firstSessionStore.get("profile"))
        assertNull(holder.storeFor("session-a").get("profile"))
        assertNotNull(secondSessionStore.get("profile"))
    }

    private class TestViewModel : ViewModel()
}
