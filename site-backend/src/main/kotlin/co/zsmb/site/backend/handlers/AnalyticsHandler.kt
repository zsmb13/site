package co.zsmb.site.backend.handlers

import co.zsmb.site.backend.data.AnalyticsEvent
import co.zsmb.site.backend.data.AnalyticsEventRepository
import co.zsmb.site.backend.data.AnalyticsSummary
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.text.SimpleDateFormat
import java.util.*


@Component
@PreAuthorize("hasRole('ADMIN')")
class AnalyticsHandler(private val analyticsEventRepository: AnalyticsEventRepository) {

    fun getAllEvents(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(analyticsEventRepository.findAll())
    }

    fun getAllEventsGrouped(req: ServerRequest): Mono<ServerResponse> {
        fun Long.formatWith(sdf: SimpleDateFormat) = sdf.format(this)
        fun SimpleDateFormat.inBudapest() = apply { timeZone = TimeZone.getTimeZone("Europe/Budapest") }
        fun Long.toDate() = formatWith(SimpleDateFormat("yyyy-MM-dd").inBudapest())
        fun Long.toDateWithHour() = formatWith(SimpleDateFormat("yyyy-MM-dd HH").inBudapest())

        return when (req.queryParam("by").orElse("endpoint")) {
            "day", "date" -> getAllEventsGroupedBy(req, { it.timestamp.toDate() })
            "hour" -> getAllEventsGroupedBy(req, { it.timestamp.toDateWithHour() })
            else -> getAllEventsGroupedByEndPoint(req)
        }
    }

    private fun getAllEventsGroupedByEndPoint(req: ServerRequest): Mono<ServerResponse> {
        val summaries: Flux<AnalyticsSummary> = analyticsEventRepository.findAll()
                .map { "${it.method} ${it.path}" }
                .collectMultimap { it }
                .flatMapIterable { it.entries }
                .sort { o1, o2 -> o1.key.compareTo(o2.key) }
                .map { AnalyticsSummary(it.key, it.value.size) }

        return ServerResponse.ok().body(summaries)
    }

    private fun getAllEventsGroupedBy(req: ServerRequest, groupingFunction: (AnalyticsEvent) -> String): Mono<ServerResponse> {
        val summaries: Flux<AnalyticsSummary> = analyticsEventRepository.findAll()
                .collectMultimap { groupingFunction(it) }
                .flatMapIterable { it.entries }
                .sort { o1, o2 -> o1.key.compareTo(o2.key) }
                .map { AnalyticsSummary(it.key, it.value.size) }

        return ServerResponse.ok().body(summaries)
    }

    fun removeAllEvents(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(analyticsEventRepository.deleteAll())
    }

}
