package co.zsmb.site.backend.extensions

import org.springframework.core.ParameterizedTypeReference
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient

inline fun <reified T : Any> WebTestClient.ResponseSpec.expectBodyAs() = expectBody(object : ParameterizedTypeReference<T>() {})

inline fun <B> WebTestClient.BodySpec<B, *>.consume(consumer: (EntityExchangeResult<B>) -> Unit) = apply {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    consumer(this.returnResult())
}

inline fun <B> WebTestClient.BodySpec<B, *>.consumeBody(consumer: (B) -> Unit) = apply {
    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    consumer(this.returnResult().responseBody)
}
