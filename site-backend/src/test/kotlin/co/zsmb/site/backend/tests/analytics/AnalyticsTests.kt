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
import org.springframework.http.MediaType
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
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<List<AnalyticsEvent>>().isEqualWith(MockData.ANALYTICS_EVENTS)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Get all analytics events grouped by default grouping as ADMIN`() {
        val summaries = MockData.ANALYTICS_EVENTS
                .groupingBy { "${it.method} ${it.path}" }
                .eachCount()
                .map { AnalyticsSummary(it.key, it.value) }
                .toSet()
        client.get()
                .uri("/analytics/grouped")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<Set<AnalyticsSummary>>().isEqualWith(summaries)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Get all analytics events grouped by endpoint as ADMIN`() {
        val summaries = MockData.ANALYTICS_EVENTS
                .groupingBy { "${it.method} ${it.path}" }
                .eachCount()
                .map { AnalyticsSummary(it.key, it.value) }
                .toSet()
        client.get()
                .uri("/analytics/grouped?by=endpoint")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<Set<AnalyticsSummary>>().isEqualWith(summaries)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Get all analytics events grouped by date as ADMIN`() {
        client.get()
                .uri("/analytics/grouped?by=date")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<List<AnalyticsSummary>>().isEqualWith(MockData.ANALYTICS_EVENTS_BY_DAY)
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Get all analytics events grouped by hour as ADMIN`() {
        client.get()
                .uri("/analytics/grouped?by=hour")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<List<AnalyticsSummary>>().isEqualWith(MockData.ANALYTICS_EVENTS_BY_HOUR)
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
