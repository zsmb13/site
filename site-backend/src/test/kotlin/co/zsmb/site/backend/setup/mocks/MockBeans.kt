package co.zsmb.site.backend.setup.mocks

import co.zsmb.site.backend.data.*
import co.zsmb.site.backend.extensions.successfulMono
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

                on { findAllByOrderByPublishDateDesc() } doReturn
                        MockData.ARTICLES.sortedByDescending { it.publishDate }.toFlux()

                on { findById(any<String>()) } doAnswer {
                    val articleId = it.arguments[0] as String
                    MockData.ARTICLES.find { it.id == articleId }?.toMono() ?: Mono.empty()
                }

                on { findByUrl(any()) } doAnswer {
                    val url = it.arguments[0] as String
                    MockData.ARTICLES.find { it.url == url }?.toMono() ?: Mono.empty()
                }

                on { save(any<Article>()) } doAnswer {
                    val user = it.arguments[0] as Article
                    user.toMono()
                }

                on { insert(any<Article>()) } doAnswer { (it.arguments[0] as Article).copy(id = MockData.ID).toMono() }

                on { deleteById(any<String>()) } doReturn successfulMono()
            }
        }

        bean(isPrimary = true) {
            mock<CustomPageRepository> {
                on { findAll() } doReturn MockData.CUSTOM_PAGES.toFlux()

                on { findByName(any()) } doAnswer {
                    val name = it.arguments[0] as String
                    MockData.CUSTOM_PAGES.find { it.name == name }?.toMono() ?: Mono.empty()
                }

                on { deleteByName(any()) } doAnswer {
                    val create: Mono<Int> = Mono.create { it.success() }
                    create
                }

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

                on { findByName(any()) } doAnswer {
                    val name = it.arguments[0] as String
                    MockData.USERS.find { it.name == name }?.toMono() ?: Mono.empty()
                }

                on { deleteById(any<String>()) } doReturn successfulMono()

                on { insert(any<User>()) } doAnswer {
                    val user = it.arguments[0] as User
                    user.copy(id = MockData.ID).toMono()
                }

                on { save(any<User>()) } doAnswer {
                    val user = it.arguments[0] as User
                    user.toMono()
                }
            }
        }

        bean(isPrimary = true) {
            mock<AnalyticsEventRepository> {
                on { insert(any<AnalyticsEvent>()) } doAnswer {
                    val event = it.arguments[0] as AnalyticsEvent
                    event.copy(id = MockData.ID).toMono()
                }

                on { deleteAll() } doReturn successfulMono()

                on { findAll() } doReturn MockData.ANALYTICS_EVENTS.toFlux()
            }
        }
    }
}
