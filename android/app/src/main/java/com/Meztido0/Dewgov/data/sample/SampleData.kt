package com.Meztido0.Dewgov.data.sample

import com.Meztido0.Dewgov.R
import com.Meztido0.Dewgov.domain.model.Badge
import com.Meztido0.Dewgov.domain.model.District
import com.Meztido0.Dewgov.domain.model.Machine
import com.Meztido0.Dewgov.domain.model.Mission
import com.Meztido0.Dewgov.domain.model.MissionGoalType
import com.Meztido0.Dewgov.domain.model.RankTier
import com.Meztido0.Dewgov.domain.model.RivalEntry
import com.Meztido0.Dewgov.domain.model.Skin
import com.Meztido0.Dewgov.domain.model.SkinPerk
import com.Meztido0.Dewgov.domain.model.SlotSymbol
import com.Meztido0.Dewgov.domain.model.SpriteKind
import com.Meztido0.Dewgov.domain.model.Volatility

object SampleData {

    val machines: List<Machine> = listOf(
        Machine(
            id = "diner",
            nameRes = R.string.machine_diner,
            weightingRes = R.string.machine_diner_weighting,
            topLine = 8,
            volatility = Volatility.LOW,
            badgeRequirement = 0,
            art = SpriteKind.ROAD_SIGN,
            markerBias = 0.8f,
            lowBias = 1.3f
        ),
        Machine(
            id = "overpass",
            nameRes = R.string.machine_overpass,
            weightingRes = R.string.machine_overpass_weighting,
            topLine = 25,
            volatility = Volatility.MEDIUM,
            badgeRequirement = 4,
            art = SpriteKind.VAN,
            markerBias = 1.0f,
            lowBias = 1.0f
        ),
        Machine(
            id = "terminal",
            nameRes = R.string.machine_terminal,
            weightingRes = R.string.machine_terminal_weighting,
            topLine = 25,
            volatility = Volatility.HIGH,
            badgeRequirement = 12,
            art = SpriteKind.MARKER,
            markerBias = 2.0f,
            lowBias = 0.8f
        )
    )

    val districts: List<District> = listOf(
        District("d1", R.string.district_diner, 0, R.string.district_diner_grant, "diner"),
        District("d2", R.string.district_overpass, 4, R.string.district_overpass_grant, "overpass"),
        District("d3", R.string.district_underpass, 8, R.string.district_underpass_grant, "overpass"),
        District("d4", R.string.district_market, 12, R.string.district_market_grant, "terminal"),
        District("d5", R.string.district_terminal, 18, R.string.district_terminal_grant, "terminal")
    )

    val skins: List<Skin> = listOf(
        Skin("neon", R.string.skin_neon, R.string.skin_neon_perk, SkinPerk.NONE, 0xFFFF006E, 0),
        Skin("chrome", R.string.skin_chrome, R.string.skin_chrome_perk, SkinPerk.EXTRA_JUNCTION, 0xFFE8ECF5, 3),
        Skin("amber", R.string.skin_amber, R.string.skin_amber_perk, SkinPerk.FIRST_FEATHER_WILD, 0xFFFFB020, 3),
        Skin("asphalt", R.string.skin_asphalt, R.string.skin_asphalt_perk, SkinPerk.EXTRA_LANE_BOOST, 0xFF9AA4BF, 3),
        Skin("market", R.string.skin_market, R.string.skin_market_perk, SkinPerk.EXTRA_JUNCTION, 0xFF00F5FF, 3),
        Skin("terminal", R.string.skin_terminal, R.string.skin_terminal_perk, SkinPerk.EXTRA_LANE_BOOST, 0xFF3DDC97, 3)
    )

    val badges: List<Badge> = listOf(
        Badge("first_feather", R.string.badge_first_feather, R.string.badge_first_feather_hint),
        Badge("triple_van", R.string.badge_triple_van, R.string.badge_triple_van_hint),
        Badge("lane_master", R.string.badge_lane_master, R.string.badge_lane_master_hint),
        Badge("night_courier", R.string.badge_night_courier, R.string.badge_night_courier_hint),
        Badge("road_scout", R.string.badge_road_scout, R.string.badge_road_scout_hint),
        Badge("chrome_touch", R.string.badge_chrome_touch, R.string.badge_chrome_touch_hint),
        Badge("marker_hunter", R.string.badge_marker_hunter, R.string.badge_marker_hunter_hint),
        Badge("feather_storm", R.string.badge_feather_storm, R.string.badge_feather_storm_hint),
        Badge("overpass_key", R.string.badge_overpass_key, R.string.badge_overpass_key_hint),
        Badge("midnight_run", R.string.badge_midnight_run, R.string.badge_midnight_run_hint),
        Badge("wild_streak", R.string.badge_wild_streak, R.string.badge_wild_streak_hint),
        Badge("terminal_pass", R.string.badge_terminal_pass, R.string.badge_terminal_pass_hint)
    )

    val missions: List<Mission> = listOf(
        Mission("night_shift", R.string.mission_night_shift, R.string.mission_night_shift_desc, MissionGoalType.SPINS, 15),
        Mission("feather_sweep", R.string.mission_feather_sweep, R.string.mission_feather_sweep_desc, MissionGoalType.FEATHERS, 20),
        Mission("route_runner", R.string.mission_route_runner, R.string.mission_route_runner_desc, MissionGoalType.BONUS_ROUTES, 2)
    )

    val ranks: List<RankTier> = listOf(
        RankTier("chick", R.string.rank_chick, 0, R.string.rank_chick_reward),
        RankTier("scout", R.string.rank_scout, 150, R.string.rank_scout_reward),
        RankTier("runner", R.string.rank_runner, 400, R.string.rank_runner_reward),
        RankTier("courier", R.string.rank_courier, 900, R.string.rank_courier_reward),
        RankTier("legend", R.string.rank_legend, 1800, R.string.rank_legend_reward)
    )

    val rivals: List<RivalEntry> = listOf(
        RivalEntry(R.string.rival_chrome_hen, 1420),
        RivalEntry(R.string.rival_midnight_rooster, 1180),
        RivalEntry(R.string.rival_feather_cab, 980),
        RivalEntry(R.string.rival_diner_pullet, 870),
        RivalEntry(R.string.rival_amber_bantam, 720),
        RivalEntry(R.string.rival_market_clucker, 540),
        RivalEntry(R.string.rival_asphalt_peep, 410),
        RivalEntry(R.string.rival_terminal_wing, 260)
    )

    val payoutOrder: List<SlotSymbol> = listOf(
        SlotSymbol.CHIKO,
        SlotSymbol.VAN,
        SlotSymbol.ROAD_SIGN,
        SlotSymbol.FEATHER,
        SlotSymbol.MARKER
    )

    fun machineOf(id: String): Machine = machines.firstOrNull { it.id == id } ?: machines.first()

    fun skinOf(id: String): Skin = skins.firstOrNull { it.id == id } ?: skins.first()
}
