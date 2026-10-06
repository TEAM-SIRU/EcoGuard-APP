package com.nativelap.ecoguard.feature.login.viewmodel

sealed interface LoginScreenEvent {
    data object LoginClick : LoginScreenEvent
}
