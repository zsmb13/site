package co.zsmb.site.backend.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "articles")
data class Article(
        val title: String? = null,
        val content: String? = null,
        val summary: String? = null,
        val publishDate: Date? = null,
        val lastModificationDate: Date? = null,
        @Id val id: String? = null)
