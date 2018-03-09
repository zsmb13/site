package co.zsmb.site.backend

import co.zsmb.site.backend.extensions.expectBodyAs
import co.zsmb.site.backend.extensions.isEqual
import co.zsmb.site.backend.setup.SpringTest
import co.zsmb.site.backend.setup.mocks.MockData
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse

@SpringTest
class ArticleTests(@Autowired routerFunction: RouterFunction<ServerResponse>) {

    private val client = WebTestClient.bindToRouterFunction(routerFunction).build()

    @Test
    fun `Get all articles`() {
        client.get()
                .uri("/articles")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<List<Article>>().isEqual(MockData.ARTICLES)
    }

    @Test
    fun `Get article by id`() {
        val article = MockData.ARTICLES[1]

        client.get()
                .uri("/articles/${article.id}")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<Article>().isEqual(article)
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
    fun `Create new article`() {
        val article = Article(title = "my title", content = "some content")
        val modifiedArticle = article.copy(id = MockData.ID)
        client.post()
                .uri("/articles")
                .syncBody(article)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectHeader().valueEquals("location", "/articles/${MockData.ID}")
                .expectBodyAs<Article>().isEqual(modifiedArticle)
    }

}
