package co.zsmb.site.backend.setup.mocks

import co.zsmb.site.backend.data.Article
import co.zsmb.site.backend.data.ArticleRepository
import co.zsmb.site.backend.data.CustomPage
import co.zsmb.site.backend.data.CustomPageRepository
import co.zsmb.site.backend.security.User
import co.zsmb.site.backend.security.UserRepository
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

                on { findByUrl(any()) } doAnswer {
                    val url = it.arguments[0] as String
                    MockData.ARTICLES.find { it.url == url }?.toMono() ?: Mono.empty()
                }

                on { insert(any<Article>()) } doAnswer { (it.arguments[0] as Article).copy(id = MockData.ID).toMono() }
            }
        }

        bean(isPrimary = true) {
            mock<CustomPageRepository> {
                on { findByName(any()) } doAnswer {
                    val name = it.arguments[0] as String
                    MockData.CUSTOM_PAGES.find { it.name == name }?.toMono() ?: Mono.empty()
                }

                on { deleteByName(any()) } doAnswer { Mono.create { it.success() } }

                on { insert(any<CustomPage>()) } doAnswer { (it.arguments[0] as CustomPage).copy(id = MockData.ID).toMono() }
            }
        }

        bean(isPrimary = true) {
            mock<UserRepository> {
                on { findAll() } doReturn MockData.USERS.toFlux()

                on { findById(any<String>()) } doAnswer {
                    val userId = it.arguments[0] as String
                    MockData.USERS.find { it.id == userId }?.toMono() ?: Mono.empty()
                }

                on { insert(any<User>()) } doAnswer { (it.arguments[0] as User).copy(id = MockData.ID).toMono() }
            }
        }
    }
}
