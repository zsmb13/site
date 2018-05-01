package co.zsmb.site.backend.handlers

import co.zsmb.site.backend.data.AnalyticsEventRepository
import co.zsmb.site.backend.data.AnalyticsSummary
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
@PreAuthorize("hasRole('ADMIN')")
class AnalyticsHandler(private val analyticsEventRepository: AnalyticsEventRepository) {

    fun getAllEvents(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(analyticsEventRepository.findAll())
    }

    fun getAllEventsGrouped(req: ServerRequest): Mono<ServerResponse> {
        val summaries: Flux<AnalyticsSummary> = analyticsEventRepository.findAll()
                .map { "${it.method} ${it.path}" }
                .collectMultimap { it }
                .flatMapIterable { it.entries }
                .map { AnalyticsSummary(it.key, it.value.size) }

        return ServerResponse.ok().body(summaries)
    }

    fun removeAllEvents(req: ServerRequest): Mono<ServerResponse> {
        return ServerResponse.ok().body(analyticsEventRepository.deleteAll())
    }

}
