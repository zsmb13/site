package co.zsmb.site.backend.tests.analytics

import co.zsmb.site.backend.data.AnalyticsEvent
import co.zsmb.site.backend.data.AnalyticsEventRepository
import co.zsmb.site.backend.setup.SpringTest
import co.zsmb.site.backend.setup.mocks.MockData
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_CLASS
import org.springframework.test.web.reactive.server.WebTestClient

@SpringTest
@DirtiesContext(classMode = AFTER_CLASS)
class AnalyticsFilterTests @Autowired constructor(
        context: ApplicationContext,
        private val analyticsEventRepository: AnalyticsEventRepository
) {

    private val client = WebTestClient.bindToApplicationContext(context).build()

    @Test
    fun `Public API requests are logged to analytics`() {
        client.get()
                .uri("/public/articlesummaries")
                .exchange()

        val captor = argumentCaptor<AnalyticsEvent>()
        verify(analyticsEventRepository).insert(captor.capture())
        reset(analyticsEventRepository)

        val event = captor.firstValue
        assertEquals("GET", event.method)
        assertEquals("/public/articlesummaries", event.path)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Private API requests are logged to analytics`() {
        client.delete()
                .uri("/articles/${MockData.ARTICLES[0].id}")
                .exchange()

        val captor = argumentCaptor<AnalyticsEvent>()
        verify(analyticsEventRepository).insert(captor.capture())
        reset(analyticsEventRepository)

        val event = captor.firstValue
        assertEquals("DELETE", event.method)
        assertEquals("/articles/${MockData.ARTICLES[0].id}", event.path)
    }

}
