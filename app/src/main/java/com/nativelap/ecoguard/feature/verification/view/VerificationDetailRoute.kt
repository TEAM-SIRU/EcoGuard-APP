package com.nativelap.ecoguard.feature.verification.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.nativelap.ecoguard.BuildConfig
import com.nativelap.ecoguard.R
import com.nativelap.ecoguard.core.presentation.rememberActivityViewModel
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationDetailUiState
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationDetailViewModel
import com.nativelap.ecoguard.feature.verification.viewmodel.VerificationResultScreenEvent
import com.nativelap.ecoguard.ui.component.BottomCtaBar
import com.nativelap.ecoguard.ui.component.CenteredScrollContent
import com.nativelap.ecoguard.ui.component.EcoBackTopBar
import com.nativelap.ecoguard.ui.component.EcoPrimaryButton
import com.nativelap.ecoguard.ui.theme.EcoGuardTheme

@Composable
fun VerificationDetailRoute(
    verificationId: Long,
    onEvent: (VerificationResultScreenEvent) -> Unit,
    viewModel: VerificationDetailViewModel? = null,
) {
    val screenViewModel = viewModel ?: rememberActivityViewModel<VerificationDetailViewModel>()
    val uiState by screenViewModel.uiState.collectAsState()

    LaunchedEffect(screenViewModel, verificationId) {
        screenViewModel.load(verificationId)
    }

    when (val state = uiState) {
        VerificationDetailUiState.Loading -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { EcoBackTopBar(onBackClick = { onEvent(VerificationResultScreenEvent.BackClick) }) },
            ) { padding ->
                CenteredScrollContent(innerPadding = padding) {
                    CircularProgressIndicator()
                }
            }
        }

        VerificationDetailUiState.Failed -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = { EcoBackTopBar(onBackClick = { onEvent(VerificationResultScreenEvent.BackClick) }) },
                bottomBar = {
                    BottomCtaBar {
                        EcoPrimaryButton(
                            text = stringResource(R.string.action_retry),
                            onClick = { screenViewModel.load(verificationId) },
                        )
                    }
                },
            ) { padding ->
                CenteredScrollContent(innerPadding = padding) {
                    Text(stringResource(R.string.activity_load_failed))
                }
            }
        }

        is VerificationDetailUiState.Content -> {
            VerificationDetailScreen(
                verificationDate = state.verificationDate,
                statusDescription = state.rejectionReason ?: stringResource(state.statusDescriptionRes),
                status = state.status,
                areaName = state.areaName,
                submittedDateTime = state.submittedDateTime,
                photoUrl = resolveVerificationPhotoUrl(state.photoUrl, BuildConfig.BASE_URL),
                onEvent = { event ->
                    if (event == VerificationResultScreenEvent.RetryClick) {
                        screenViewModel.load(verificationId)
                    } else {
                        onEvent(event)
                    }
                },
            )
        }
    }
}

internal fun resolveVerificationPhotoUrl(
    photoUrl: String?,
    baseUrl: String,
): String? {
    val candidate = photoUrl?.takeIf { value -> value.isNotBlank() && value == value.trim() } ?: return null
    if (!candidate.startsWith("/files/") || candidate.startsWith("//")) return null
    if ('\\' in candidate || '?' in candidate || '#' in candidate) return null

    val pathUri = runCatching { java.net.URI(candidate) }.getOrNull() ?: return null
    if (pathUri.isAbsolute || pathUri.rawAuthority != null || pathUri.rawQuery != null || pathUri.rawFragment != null) {
        return null
    }
    val rawPath = pathUri.rawPath ?: return null
    if (ENCODED_PATH_SEPARATOR_OR_DOT.containsMatchIn(rawPath) || ENCODED_PERCENT.containsMatchIn(rawPath)) return null
    val decodedPath =
        runCatching {
            java.net.URLDecoder.decode(rawPath.replace("+", "%2B"), Charsets.UTF_8.name())
        }.getOrNull() ?: return null
    val pathSegments = decodedPath.split('/')
    if (pathSegments.size < 3 || pathSegments[1] != "files") return null
    if (pathSegments.drop(2).any { segment -> segment.isBlank() || segment == "." || segment == ".." }) return null
    if (pathUri.normalize().rawPath != rawPath) return null

    val apiUri = runCatching { java.net.URI(baseUrl) }.getOrNull() ?: return null
    if (apiUri.scheme != "https" || apiUri.host.isNullOrBlank() || apiUri.rawUserInfo != null) return null
    if (apiUri.rawQuery != null || apiUri.rawFragment != null) return null
    val resolvedUri = apiUri.resolve(pathUri)
    if (resolvedUri.scheme != "https" || !resolvedUri.host.equals(apiUri.host, ignoreCase = true)) return null
    if (effectivePort(resolvedUri) != effectivePort(apiUri)) return null
    if (!resolvedUri.rawPath.orEmpty().startsWith("/files/")) return null
    return resolvedUri.toASCIIString()
}

private fun effectivePort(uri: java.net.URI): Int = if (uri.port == -1) 443 else uri.port

private val ENCODED_PATH_SEPARATOR_OR_DOT = Regex("(?i)%2f|%5c|%2e")
private val ENCODED_PERCENT = Regex("(?i)%25")

@Preview
@Composable
private fun VerificationDetailRouteLoadingPreview() {
    EcoGuardTheme {
        Scaffold { padding ->
            CenteredScrollContent(innerPadding = padding) { Text(stringResource(R.string.common_loading)) }
        }
    }
}
