package co.zsmb.site.backend.tests.analytics

import co.zsmb.site.backend.data.AnalyticsEvent
import co.zsmb.site.backend.data.AnalyticsSummary
import co.zsmb.site.backend.extensions.expectBodyAs
import co.zsmb.site.backend.extensions.isEqualWith
import co.zsmb.site.backend.setup.SpringTest
import co.zsmb.site.backend.setup.mocks.MockData
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient

@SpringTest
class AnalyticsTests @Autowired constructor(context: ApplicationContext) {

    private val client = WebTestClient.bindToApplicationContext(context).build()

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Get all analytics events as ADMIN`() {
        client.get()
                .uri("/analytics")
                .exchange()
                .expectStatus().isOk()
                .expectBodyAs<List<AnalyticsEvent>>().isEqualWith(MockData.ANALYTICS_EVENTS)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Get all analytics events grouped as ADMIN`() {
        val summaries = MockData.ANALYTICS_EVENTS
                .groupingBy { "${it.method} ${it.path}" }
                .eachCount()
                .map { AnalyticsSummary(it.key, it.value) }
                .toSet()
        client.get()
                .uri("/analytics/grouped")
                .exchange()
                .expectStatus().isOk()
                .expectBodyAs<Set<AnalyticsSummary>>().isEqualWith(summaries)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Delete all analytics events as ADMIN`() {
        client.delete()
                .uri("/analytics")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty()
    }

}

