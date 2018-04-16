package co.zsmb.site.common

import kotlinx.serialization.Serializable

@Serializable
data class ArticleSummary(
        val title: String? = null,
        val summary: String? = null,
        val publishDate: Long? = null,
        val id: String? = null
)

@Serializable
data class ArticleDetail(
        val title: String? = null,
        val content: String? = null,
        val publishDate: Long? = null,
        val lastModificationDate: Long? = null,
        val id: String? = null
)
