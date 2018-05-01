package co.zsmb.site.backend.data

import kotlinx.serialization.Serializable

@Serializable
data class AnalyticsSummary(val name: String, val count: Int)
