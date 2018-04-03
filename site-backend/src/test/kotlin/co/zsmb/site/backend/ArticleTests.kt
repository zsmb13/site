package co.zsmb.site.backend

import co.zsmb.site.backend.extensions.expectBodyAs
import co.zsmb.site.backend.extensions.isEqualWith
import co.zsmb.site.backend.setup.SpringTest
import co.zsmb.site.backend.setup.mocks.MockData
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient

@SpringTest
class ArticleTests(@Autowired context: ApplicationContext) {

    private val client = WebTestClient.bindToApplicationContext(context).build()

    @Test
    fun `Get all article`() {
        client.get()
                .uri("/articles")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<List<Article>>().isEqualWith(MockData.ARTICLES)
    }

    @Test
    fun `Get article by id`() {
        val article = MockData.ARTICLES[1]

        client.get()
                .uri("/articles/${article.id}")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<Article>().isEqualWith(article)
    }

    @Test
    fun `Get article with invalid id`() {
        client.get()
                .uri("/articles/${MockData.NON_EXISTENT_ID}")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty()
    }

    @Test
    fun `Create new article without auth`() {
        val article = Article(title = "my title", content = "some content")
        val modifiedArticle = article.copy(id = MockData.ID)
        client.post()
                .uri("/articles")
                .syncBody(article)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().isEmpty()
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Create new article as ADMIN`() {
        val article = Article(title = "my title", content = "some content")
        val modifiedArticle = article.copy(id = MockData.ID)
        client.post()
                .uri("/articles")
                .syncBody(article)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectHeader().valueEquals("location", "/articles/${MockData.ID}")
                .expectBodyAs<Article>().isEqualWith(modifiedArticle)
    }

}
