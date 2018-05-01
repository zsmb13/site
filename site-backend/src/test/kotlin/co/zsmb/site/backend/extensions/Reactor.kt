package co.zsmb.site.backend.extensions

import reactor.core.publisher.Mono

fun <T> successfulMono(): Mono<T> = Mono.create { it.success() }
