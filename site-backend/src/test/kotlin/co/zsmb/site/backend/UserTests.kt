package co.zsmb.site.backend

import co.zsmb.site.backend.extensions.expectBodyAs
import co.zsmb.site.backend.extensions.isEqualWith
import co.zsmb.site.backend.security.User
import co.zsmb.site.backend.setup.SpringTest
import co.zsmb.site.backend.setup.mocks.MockData
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient

@SpringTest
class UserTests(@Autowired context: ApplicationContext) {

    val client: WebTestClient = WebTestClient
            .bindToApplicationContext(context)
            .build()

    @Test
    fun `Get users with no auth`() {
        client.get()
                .uri("/users")
                .exchange()
                .expectStatus().isUnauthorized()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `Get users as USER`() {
        client.get()
                .uri("/users")
                .exchange()
                .expectStatus().isForbidden()
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Get users as ADMIN`() {
        client.get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<List<User>>().isEqualWith(MockData.USERS)
    }

}
