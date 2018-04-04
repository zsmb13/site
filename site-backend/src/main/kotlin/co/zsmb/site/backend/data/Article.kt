package co.zsmb.site.backend.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "articles")
data class Article(
        val title: String? = null,
        val content: String? = null,
        @Id val id: String? = null)
