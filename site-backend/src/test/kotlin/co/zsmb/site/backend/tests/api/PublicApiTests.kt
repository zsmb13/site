package co.zsmb.site.backend.tests.api

import co.zsmb.site.backend.data.Article
import co.zsmb.site.backend.data.toDetail
import co.zsmb.site.backend.data.toSummary
import co.zsmb.site.backend.extensions.expectBodyAs
import co.zsmb.site.backend.extensions.isEqualWith
import co.zsmb.site.backend.setup.SpringTest
import co.zsmb.site.backend.setup.mocks.MockData
import co.zsmb.site.common.ArticleDetail
import co.zsmb.site.common.ArticleSummary
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient

@SpringTest
class PublicApiTests(@Autowired context: ApplicationContext) {

    private val client = WebTestClient.bindToApplicationContext(context).build()

    @Test
    fun `Get article summaries`() {
        val summaries = MockData.ARTICLES.map(Article::toSummary)
        client.get()
                .uri("/public/articlesummaries")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<List<ArticleSummary>>().isEqualWith(summaries)
    }

    @Test
    fun `Get article detail`() {
        val detail = MockData.ARTICLES[1].let(Article::toDetail)
        client.get()
                .uri("/public/articledetails/${detail.id}")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<ArticleDetail>().isEqualWith(detail)
    }

}
