package co.zsmb.site.backend

import co.zsmb.site.backend.setup.SpringTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse

@SpringTest
class HelloTest(@Autowired routerFunction: RouterFunction<ServerResponse>) {

    val client: WebTestClient = WebTestClient.bindToRouterFunction(routerFunction).build()

    @Test
    fun `Check API connection`() {
        client.get()
                .uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty()
    }

}
