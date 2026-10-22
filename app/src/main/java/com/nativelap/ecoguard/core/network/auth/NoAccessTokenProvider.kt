package com.nativelap.ecoguard.core.network.auth

import javax.inject.Inject

class NoAccessTokenProvider
    @Inject
    constructor() : AccessTokenProvider {
        override fun getAccessToken(): String? = null
    }
