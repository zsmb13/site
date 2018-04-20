package co.zsmb.site.backend.extensions

import reactor.core.publisher.Mono

fun monoOfVoid(): Mono<Void> = Mono.just("").then()
