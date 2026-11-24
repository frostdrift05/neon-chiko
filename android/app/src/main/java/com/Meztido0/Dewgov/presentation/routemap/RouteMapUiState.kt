package com.Meztido0.Dewgov.presentation.routemap

import com.Meztido0.Dewgov.domain.model.District

data class RouteNode(
    val district: District,
    val unlocked: Boolean,
    val current: Boolean
)

data class RouteMapUiState(
    val nodes: List<RouteNode> = emptyList(),
    val badgeCount: Int = 0,
    val expandedId: String = ""
)
