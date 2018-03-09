package co.zsmb.site.backend.setup.mocks

import co.zsmb.site.backend.Article
import co.zsmb.site.backend.ArticleRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doAnswer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.springframework.context.support.beans
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono

fun testBeans() = beans {
    profile("test") {
        bean(isPrimary = true) {
            mock<ArticleRepository> {
                on { findAll() } doReturn MockData.ARTICLES.toFlux()
                on { findById(any<String>()) } doAnswer {
                    val articleId = it.arguments[0] as String
                    MockData.ARTICLES.find { it.id == articleId }?.toMono() ?: Mono.empty()
                }
                on { insert(any<Article>()) } doAnswer {
                    val article = it.arguments[0] as Article
                    article.copy(id = MockData.ID).toMono()
                }
            }
        }
    }
}
