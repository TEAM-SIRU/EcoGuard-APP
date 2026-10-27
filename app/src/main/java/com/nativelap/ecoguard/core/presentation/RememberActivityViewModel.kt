package com.nativelap.ecoguard.core.presentation

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

val LocalFeatureSessionId = compositionLocalOf { "" }

/** Resolves a Hilt ViewModel from a session-scoped store without a navigation-compose artifact. */
@Composable
inline fun <reified ViewModelType : ViewModel> rememberActivityViewModel(routeKey: String? = null): ViewModelType {
    val context = LocalContext.current
    val owner = context.findActivity()
    val sessionId = LocalFeatureSessionId.current
    val storeHolder = remember(owner) { ViewModelProvider(owner)[FeatureViewModelStoreHolder::class.java] }
    val sessionStore = remember(storeHolder, sessionId) { storeHolder.storeFor(sessionId) }
    val modelKey = routeKey ?: ViewModelType::class.java.canonicalName.orEmpty()
    return remember(sessionStore, modelKey) {
        ViewModelProvider(sessionStore, owner.defaultViewModelProviderFactory)[modelKey, ViewModelType::class.java]
    }
}

fun clearFeatureViewModelsForSession(
    owner: androidx.lifecycle.ViewModelStoreOwner,
    sessionId: String,
) {
    ViewModelProvider(owner)[FeatureViewModelStoreHolder::class.java].clearSession(sessionId)
}

class FeatureViewModelStoreHolder : ViewModel() {
    private val stores = mutableMapOf<String, ViewModelStore>()

    fun storeFor(sessionId: String): ViewModelStore = stores.getOrPut(sessionId, ::ViewModelStore)

    fun clearSession(sessionId: String) {
        stores.remove(sessionId)?.clear()
    }

    override fun onCleared() {
        stores.values.forEach(ViewModelStore::clear)
        stores.clear()
    }
}

@PublishedApi
internal tailrec fun Context.findActivity(): androidx.activity.ComponentActivity =
    when (this) {
        is androidx.activity.ComponentActivity -> this
        is ContextWrapper -> baseContext.findActivity()
        else -> error("A Hilt ComponentActivity is required to create this ViewModel")
    }
