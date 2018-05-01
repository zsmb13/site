package co.zsmb.site.backend.handlers

import co.zsmb.site.backend.data.AnalyticsEvent
import co.zsmb.site.backend.data.AnalyticsEventRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.HandlerFilterFunction
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class AnalyticsFilter(
        private val analyticsEventRepository: AnalyticsEventRepository
) : HandlerFilterFunction<ServerResponse, ServerResponse> {

    override fun filter(request: ServerRequest, next: HandlerFunction<ServerResponse>): Mono<ServerResponse> {
        val event = AnalyticsEvent(
                timestamp = System.currentTimeMillis(),
                method = request.methodName(),
                path = request.path()
        )

        return analyticsEventRepository.insert(event)
                .then(next.handle(request))
    }

}
