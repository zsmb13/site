package co.zsmb.site.backend.tests.api

import co.zsmb.site.backend.data.Article
import co.zsmb.site.backend.data.ArticleRepository
import co.zsmb.site.backend.data.toDetail
import co.zsmb.site.backend.extensions.consumeBody
import co.zsmb.site.backend.extensions.expectBodyAs
import co.zsmb.site.backend.extensions.isEqualWith
import co.zsmb.site.backend.setup.SpringTest
import co.zsmb.site.backend.setup.mocks.MockData
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*

@SpringTest
class ArticleTests(
        @Autowired context: ApplicationContext,
        @Autowired val articleRepository: ArticleRepository
) {

    private val client = WebTestClient.bindToApplicationContext(context).build()

    @Test
    fun `Get all articles without auth`() {
        client.get()
                .uri("/articles")
                .exchange()
                .expectStatus().isUnauthorized()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `Get all articles as USER`() {
        client.get()
                .uri("/articles")
                .exchange()
                .expectStatus().isForbidden()
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Get all articles as ADMIN`() {
        client.get()
                .uri("/articles")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<List<Article>>().isEqualWith(MockData.ARTICLES)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
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
    fun `Get article by id without auth`() {
        val article = MockData.ARTICLES[1].toDetail()

        client.get()
                .uri("/articles/${article.id}")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().isEmpty()
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Get article with invalid id`() {
        client.get()
                .uri("/articles/${MockData.NON_EXISTENT_ID}")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty()
    }

    @Test
    fun `Create new article without auth`() {
        val article = Article(title = "my title", url = "my-article", summary = "some...", content = "some content", publishDate = Date())

        client.post()
                .uri("/articles")
                .syncBody(article)
                .exchange()
                .expectStatus().isUnauthorized()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `Create new article as USER`() {
        val article = Article(title = "my title", url = "my-article", summary = "some...", content = "some content", publishDate = Date())
        client.post()
                .uri("/articles")
                .syncBody(article)
                .exchange()
                .expectStatus().isForbidden()
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Create new article as ADMIN`() {
        val article = Article(title = "my title", url = "my-article", summary = "some...", content = "some content", publishDate = Date())
        client.post()
                .uri("/articles")
                .syncBody(article)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectHeader().valueEquals("location", "/articles/${MockData.ID}")
                .expectBodyAs<Article>().consumeBody { createdArticle ->
                    assertEquals(article.title, createdArticle.title)
                    assertEquals(article.content, createdArticle.content)
                    assertEquals(article.url, createdArticle.url)
                    assertEquals(article.summary, createdArticle.summary)
                    assertEquals(article.content, createdArticle.content)
                    assertEquals(MockData.ID, createdArticle.id)
                }
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Update article as ADMIN`() {
        val article = MockData.ARTICLES[2]
        val updatedArticle = article.copy(content = "new content", publishDate = Date(2512632L), id = "dgsmdfog3gdsg")
        client.put()
                .uri("/articles/${article.id}")
                .syncBody(updatedArticle)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<Article>().consumeBody { responseArticle ->
                    assertEquals(updatedArticle.content, responseArticle.content)
                    assertEquals(article.id, responseArticle.id)
                    assertEquals(updatedArticle.publishDate, responseArticle.publishDate)
                }
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Remove article as ADMIN`() {
        val article = MockData.ARTICLES[1]
        client.delete()
                .uri("/articles/${article.id}")
                .exchange()
                .expectStatus().isOk()
                .expectBodyAs<Article>().isEqualWith(article)

        verify(articleRepository).deleteById(article.id)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Attempt to remove non-existent article as ADMIN`() {
        client.delete()
                .uri("/articles/${MockData.NON_EXISTENT_ID}")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().isEmpty()
    }

    @Test
    fun `Remove article with no auth`() {
        val article = MockData.ARTICLES[1]
        client.delete()
                .uri("/articles/${article.id}")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().isEmpty()
    }

}
