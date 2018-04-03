package co.zsmb.site.backend

import co.zsmb.site.backend.setup.SpringTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient

@SpringTest
class HelloTest(@Autowired context: ApplicationContext) {

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
    @WithMockUser(roles = ["ADMIN"])
    fun `Check API connection as ADMIN`() {
        client.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty()
    }

}
