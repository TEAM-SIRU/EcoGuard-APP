package com.nativelap.ecoguard

import android.content.Intent
import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertNull
import org.junit.Test

class MainActivityAuthLinkTest {
    @Test
    fun newIntentConsumesAnUnmatchedHandoffLink() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val activity = instrumentation.startActivitySync(mainActivityIntent()) as MainActivity

        try {
            instrumentation.runOnMainSync {
                InstrumentationRegistry.getInstrumentation().callActivityOnNewIntent(activity, handoffIntent())
                assertNull(activity.intent.data)
            }
        } finally {
            instrumentation.runOnMainSync { activity.finish() }
        }
    }

    private fun mainActivityIntent(): Intent =
        Intent(Intent.ACTION_MAIN)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .setClassName("com.nativelap.ecoguard", MainActivity::class.java.name)

    private fun handoffIntent(): Intent =
        Intent(Intent.ACTION_VIEW)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .setClassName(
                "com.nativelap.ecoguard",
                MainActivity::class.java.name,
            ).setData(
                Uri.parse("$APP_LINK?handoff_code=unused&handoff_challenge=unknown-attempt"),
            )

    private companion object {
        const val APP_LINK = "https://ecoguard.https.gsmsv.site/auth/mobile-callback"
    }
}
