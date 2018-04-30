package co.zsmb.site.backend.tests.api

import co.zsmb.site.backend.data.Article
import co.zsmb.site.backend.extensions.consumeBody
import co.zsmb.site.backend.extensions.expectBodyAs
import co.zsmb.site.backend.extensions.isEqualWith
import co.zsmb.site.backend.security.User
import co.zsmb.site.backend.setup.SpringTest
import co.zsmb.site.backend.setup.mocks.MockData
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*

@SpringTest
class UserTests(@Autowired context: ApplicationContext, @Autowired private val passwordEncoder: PasswordEncoder) {

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

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Create user as ADMIN`() {
        val user = User("Sally", "12345", true, arrayOf("ROLE_TEST"))
        client.post()
                .uri("/users")
                .syncBody(user)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<User>().consumeBody { createdUser ->
                    assertEquals(MockData.ID, createdUser.id)
                    assertEquals(user.name, createdUser.name)
                    assertEquals(user.active, createdUser.active)
                    assertArrayEquals(user.roles, createdUser.roles)
                    assertTrue(passwordEncoder.matches(user.password, createdUser.password))
                }
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Update user as ADMIN`() {
        val user = MockData.USERS[2]
        client.put()
                .uri("/users/${user.id}")
                .syncBody(user)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<User>().consumeBody { createdUser ->
                    assertEquals(user.id, createdUser.id)
                    assertEquals(user.name, createdUser.name)
                    assertEquals(user.active, createdUser.active)
                    assertArrayEquals(user.roles, createdUser.roles)
                    assertTrue(passwordEncoder.matches(user.password, createdUser.password))
                }
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Remove user as ADMIN`() {
        client.delete()
                .uri("/users/${MockData.USERS[0].id}")
                .exchange()
                .expectStatus().isOk()
                .expectBody().isEmpty()
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Create user as ADMIN with missing body`() {
        client.post()
                .uri("/users")
                .exchange()
                .expectStatus().isBadRequest()
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Create user as ADMIN with bad format`() {
        client.post()
                .uri("/users")
                .syncBody(Article(title = "foo", url = "foo", summary = "ba...", content = "bar", publishDate = Date()))
                .exchange()
                .expectStatus().isBadRequest()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `Create user as USER`() {
        val user = User("Sally", "12345", true, arrayOf())
        client.post()
                .uri("/users")
                .syncBody(user)
                .exchange()
                .expectStatus().isForbidden()
    }

    @Test
    fun `Create user with no auth`() {
        val user = User("Sally", "12345", true, arrayOf())
        client.post()
                .uri("/users")
                .syncBody(user)
                .exchange()
                .expectStatus().isUnauthorized()
    }

}
