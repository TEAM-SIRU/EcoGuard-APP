package com.nativelap.ecoguard.feature.appeal.module.domain.model

data class Appeal(
    val appealId: Long?,
    val verificationId: Long?,
    val round: Int?,
    val studentName: String?,
    val studentNumber: String?,
    val areaName: String?,
    val verificationDate: String?,
    val photoUrl: String?,
    val failReasons: List<String>?,
    val content: String?,
    val photoUrls: List<String>?,
    val status: AppealStatus?,
    val replyTitle: String?,
    val reply: String?,
    val createdAt: String?,
)

data class MyAppeal(
    val appealId: Long?,
    val verificationId: Long?,
    val round: Int?,
    val areaName: String?,
    val verificationDate: String?,
    val content: String?,
    val photoUrls: List<String>?,
    val status: AppealStatus?,
    val awardedMinutes: Int?,
    val replyTitle: String?,
    val reply: String?,
    val createdAt: String?,
)

sealed interface AppealStatus {
    data object Pending : AppealStatus

    data object Approved : AppealStatus

    data object Rejected : AppealStatus

    data class Unknown(
        val value: String,
    ) : AppealStatus
}

data class AppealDecision(
    val decision: String? = null,
    val reply: String? = null,
    val replyTitle: String? = null,
)
