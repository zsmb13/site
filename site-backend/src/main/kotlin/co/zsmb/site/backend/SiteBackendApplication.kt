package co.zsmb.site.backend

import co.zsmb.site.backend.beans.beans
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.repository.ReactiveMongoRepository


@SpringBootApplication
class SiteBackendApplication

fun main(args: Array<String>) {
    runApplication<SiteBackendApplication>(*args) {
        addInitializers(beans())
    }
}

@Document(collection = "articles")
data class Article(
        val title: String? = null,
        val content: String? = null,
        @Id val id: String? = null)

interface ArticleRepository : ReactiveMongoRepository<Article, String>
