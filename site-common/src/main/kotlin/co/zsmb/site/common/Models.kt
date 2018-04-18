package co.zsmb.site.common

import kotlinx.serialization.Serializable

@Serializable
data class ArticleSummary(
        val title: String,
        val url: String,
        val summary: String,
        val publishDate: Long? = null,
        val id: String
)

@Serializable
data class ArticleDetail(
        val title: String,
        val content: String,
        val publishDate: Long? = null,
        val lastModificationDate: Long? = null,
        val id: String
)

@Serializable
data class CustomPage(
        val name: String,
        val content: String,
        val id: String)
