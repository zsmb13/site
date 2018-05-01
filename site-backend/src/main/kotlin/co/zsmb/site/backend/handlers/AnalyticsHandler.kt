package co.zsmb.site.backend.handlers

import co.zsmb.site.backend.data.AnalyticsEventRepository
import co.zsmb.site.backend.data.AnalyticsSummary
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux

@Component
@PreAuthorize("hasRole('ADMIN')")
class AnalyticsHandler(private val analyticsEventRepository: AnalyticsEventRepository) {

    fun getAllEvents(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(analyticsEventRepository.findAll())
    }

    fun getAllEventsGrouped(req: ServerRequest): Mono<ServerResponse> {
        val summaries = analyticsEventRepository.findAll()
                .map { "${it.method} ${it.path}" }
                .toIterable()
                .groupingBy { it }
                .eachCount()
                .map { (name, count) -> AnalyticsSummary(name, count) }
                .toFlux()

        return ServerResponse.ok().body(summaries)
    }

    fun removeAllEvents(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(analyticsEventRepository.deleteAll())
    }

}
