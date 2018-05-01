package co.zsmb.site.backend.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class AnalyticsEvent(val timestamp: Long,
                          val method: String,
                          val path: String,
                          @Id val id: String? = null)
