@file:Suppress("NOTHING_TO_INLINE")

package co.zsmb.site.backend.extensions

import org.junit.jupiter.api.Assertions
import org.springframework.test.web.reactive.server.WebTestClient

inline fun <B> WebTestClient.BodySpec<B, *>.isEqualWith(expected: B) =
        apply { Assertions.assertEquals(expected, this.returnResult().responseBody) }
