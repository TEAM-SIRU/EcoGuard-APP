package com.nativelap.ecoguard.core.network.auth

interface AccessTokenProvider {
    fun getAccessToken(): String?
}
