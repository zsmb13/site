@file:Suppress("NOTHING_TO_INLINE")

package co.zsmb.site.backend.extensions

import org.junit.jupiter.api.Assertions
import org.springframework.test.web.reactive.server.WebTestClient

inline fun <B> WebTestClient.BodySpec<B, *>.isEqual(expected: B) = this.apply {
    Assertions.assertEquals(expected, this.returnResult().responseBody)
}
