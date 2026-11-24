package com.Meztido0.Dewgov.presentation.responsible

data class ResponsiblePlayUiState(
    val sessionSeconds: Long = 0L,
    val spinsToday: Int = 0,
    val credits: Int = 0,
    val reminderEnabled: Boolean = false,
    val intervalMinutes: Int = 30,
    val showReminder: Boolean = false,
    val showResetConfirm: Boolean = false
)
