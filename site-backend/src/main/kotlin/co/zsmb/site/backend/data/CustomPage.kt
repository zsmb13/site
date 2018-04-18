package co.zsmb.site.backend.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "custom_pages")
data class CustomPage(
        val name: String,
        val content: String,
        @Id val id: String? = null)
