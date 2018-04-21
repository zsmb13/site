package co.zsmb.site.backend.handlers.util

import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.badRequest
import reactor.core.publisher.Mono

@Suppress("NOTHING_TO_INLINE")
inline fun Mono<ServerResponse>.withBoom() = this
        .switchIfEmpty(badRequest().build())
        .onErrorResume { badRequest().syncBody("Boom: ${it.message}") }
