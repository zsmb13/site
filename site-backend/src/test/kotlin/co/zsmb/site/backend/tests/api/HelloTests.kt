package co.zsmb.site.backend.tests.api

import co.zsmb.site.backend.setup.SpringTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient

@SpringTest
class HelloTests(@Autowired context: ApplicationContext) {

    val client: WebTestClient = WebTestClient
            .bindToApplicationContext(context)
            .build()

    @Test
    fun `Check API connection with no auth`() {
        client.get()
                .uri("/")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().isEmpty()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `Check API connection as USER`() {
        client.get()
                .uri("/")
                .exchange()
                .expectStatus().isForbidden()
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Check API connection as ADMIN`() {
        client.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty()
    }

}
