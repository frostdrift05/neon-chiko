package com.Meztido0.Dewgov.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Meztido0.Dewgov.domain.model.GameSettings
import com.Meztido0.Dewgov.domain.model.PlayerProfile
import com.Meztido0.Dewgov.domain.model.ReelSpeed

class PreferencesStore(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun readProfile(): PlayerProfile {
        val defaults = PlayerProfile()
        return PlayerProfile(
            credits = prefs.getInt(KEY_CREDITS, defaults.credits),
            routePoints = prefs.getInt(KEY_RP, defaults.routePoints),
            badges = prefs.getStringSet(KEY_BADGES, emptySet())?.toSet() ?: emptySet(),
            unlockedSkins = prefs.getStringSet(KEY_SKINS, null)?.toSet() ?: defaults.unlockedSkins,
            equippedSkin = prefs.getString(KEY_EQUIPPED, defaults.equippedSkin) ?: defaults.equippedSkin,
            skinShards = prefs.getInt(KEY_SHARDS, defaults.skinShards),
            selectedMachine = prefs.getString(KEY_MACHINE, defaults.selectedMachine) ?: defaults.selectedMachine,
            laneBoost = prefs.getInt(KEY_BOOST, defaults.laneBoost),
            lossStreak = prefs.getInt(KEY_LOSS_STREAK, defaults.lossStreak),
            bigWinCooldown = prefs.getInt(KEY_COOLDOWN, defaults.bigWinCooldown),
            spinsToday = prefs.getInt(KEY_SPINS_TODAY, defaults.spinsToday),
            lastRefillDay = prefs.getString(KEY_REFILL_DAY, defaults.lastRefillDay) ?: ""
        )
    }

    fun writeProfile(profile: PlayerProfile) {
        prefs.edit()
            .putInt(KEY_CREDITS, profile.credits)
            .putInt(KEY_RP, profile.routePoints)
            .putStringSet(KEY_BADGES, profile.badges)
            .putStringSet(KEY_SKINS, profile.unlockedSkins)
            .putString(KEY_EQUIPPED, profile.equippedSkin)
            .putInt(KEY_SHARDS, profile.skinShards)
            .putString(KEY_MACHINE, profile.selectedMachine)
            .putInt(KEY_BOOST, profile.laneBoost)
            .putInt(KEY_LOSS_STREAK, profile.lossStreak)
            .putInt(KEY_COOLDOWN, profile.bigWinCooldown)
            .putInt(KEY_SPINS_TODAY, profile.spinsToday)
            .putString(KEY_REFILL_DAY, profile.lastRefillDay)
            .apply()
    }

    fun readSettings(): GameSettings {
        val defaults = GameSettings()
        val speedName = prefs.getString(KEY_SPEED, defaults.reelSpeed.name) ?: defaults.reelSpeed.name
        val speed = ReelSpeed.entries.firstOrNull { it.name == speedName } ?: defaults.reelSpeed
        return GameSettings(
            reelSpeed = speed,
            nearWinHighlight = prefs.getBoolean(KEY_NEAR_WIN, defaults.nearWinHighlight),
            reducedMotion = prefs.getBoolean(KEY_REDUCED_MOTION, defaults.reducedMotion),
            payoutTableOnStart = prefs.getBoolean(KEY_PAYOUT_TABLE, defaults.payoutTableOnStart),
            runSpins = prefs.getInt(KEY_RUN_SPINS, defaults.runSpins),
            breakReminderEnabled = prefs.getBoolean(KEY_BREAK_ON, defaults.breakReminderEnabled),
            breakIntervalMinutes = prefs.getInt(KEY_BREAK_INTERVAL, defaults.breakIntervalMinutes)
        )
    }

    fun writeSettings(settings: GameSettings) {
        prefs.edit()
            .putString(KEY_SPEED, settings.reelSpeed.name)
            .putBoolean(KEY_NEAR_WIN, settings.nearWinHighlight)
            .putBoolean(KEY_REDUCED_MOTION, settings.reducedMotion)
            .putBoolean(KEY_PAYOUT_TABLE, settings.payoutTableOnStart)
            .putInt(KEY_RUN_SPINS, settings.runSpins)
            .putBoolean(KEY_BREAK_ON, settings.breakReminderEnabled)
            .putInt(KEY_BREAK_INTERVAL, settings.breakIntervalMinutes)
            .apply()
    }

    fun readMissionDay(): String = prefs.getString(KEY_MISSION_DAY, "") ?: ""

    fun writeMissionDay(day: String) {
        prefs.edit().putString(KEY_MISSION_DAY, day).apply()
    }

    fun readMissionProgress(id: String): Int = prefs.getInt(PREFIX_MISSION + id, 0)

    fun readMissionClaimed(id: String): Boolean = prefs.getBoolean(PREFIX_CLAIMED + id, false)

    fun writeMission(id: String, progress: Int, claimed: Boolean) {
        prefs.edit()
            .putInt(PREFIX_MISSION + id, progress)
            .putBoolean(PREFIX_CLAIMED + id, claimed)
            .apply()
    }

    fun clearMissions(ids: List<String>) {
        val editor = prefs.edit()
        ids.forEach { id ->
            editor.putInt(PREFIX_MISSION + id, 0)
            editor.putBoolean(PREFIX_CLAIMED + id, false)
        }
        editor.apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    private companion object {
        const val STORE_NAME = "neon_chiko_store"
        const val KEY_CREDITS = "credits"
        const val KEY_RP = "route_points"
        const val KEY_BADGES = "badges"
        const val KEY_SKINS = "skins"
        const val KEY_EQUIPPED = "equipped_skin"
        const val KEY_SHARDS = "skin_shards"
        const val KEY_MACHINE = "selected_machine"
        const val KEY_BOOST = "lane_boost"
        const val KEY_LOSS_STREAK = "loss_streak"
        const val KEY_COOLDOWN = "big_win_cooldown"
        const val KEY_SPINS_TODAY = "spins_today"
        const val KEY_REFILL_DAY = "refill_day"
        const val KEY_SPEED = "reel_speed"
        const val KEY_NEAR_WIN = "near_win"
        const val KEY_REDUCED_MOTION = "reduced_motion"
        const val KEY_PAYOUT_TABLE = "payout_table"
        const val KEY_RUN_SPINS = "run_spins"
        const val KEY_BREAK_ON = "break_on"
        const val KEY_BREAK_INTERVAL = "break_interval"
        const val KEY_MISSION_DAY = "mission_day"
        const val PREFIX_MISSION = "mission_"
        const val PREFIX_CLAIMED = "claimed_"
    }
}
