package com.Meztido0.Dewgov.domain.usecase

class RefreshDailyMissionsUseCase {

    operator fun invoke(storedDay: String, today: String): Boolean {
        return storedDay != today
    }
}
