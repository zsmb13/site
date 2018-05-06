package co.zsmb.site.backend.tests.api

import co.zsmb.site.backend.data.CustomPage
import co.zsmb.site.backend.data.render
import co.zsmb.site.backend.extensions.expectBodyAs
import co.zsmb.site.backend.extensions.isEqualWith
import co.zsmb.site.backend.setup.SpringTest
import co.zsmb.site.backend.setup.mocks.MockData
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.reactive.server.WebTestClient
import co.zsmb.site.common.CustomPage as CommonCustomPage

@SpringTest
class CustomPageTests(@Autowired context: ApplicationContext) {

    private val client = WebTestClient.bindToApplicationContext(context).build()

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Create a custom page as ADMIN`() {
        val customPage = CustomPage("about", "# About\\n Some text about me")
        val renderedPage = customPage.copy(id = MockData.ID).render()
        client.post()
                .uri("/custompages/${customPage.name}")
                .syncBody(customPage)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectStatus().isCreated()
                .expectBodyAs<CommonCustomPage>().isEqualWith(renderedPage)
    }

    @Test
    fun `Create a custom page without auth`() {
        val customPage = CustomPage("", "")
        client.post()
                .uri("/custompages/profile")
                .syncBody(customPage)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().isEmpty()
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Get all custom pages as ADMIN`() {
        client.get()
                .uri("/custompages")
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectStatus().isOk()
                .expectBodyAs<List<CustomPage>>().isEqualWith(MockData.CUSTOM_PAGES)
    }

    @Test
    fun `Get all custom pages without auth`() {
        client.get()
                .uri("/custompages")
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody().isEmpty()
    }

    @Test
    @WithMockUser(roles = ["ADMIN"])
    fun `Create a custom page with wrong name in body as ADMIN`() {
        val customPage = CustomPage("about", "# About\\n Some text about me")
        val renderedPage = customPage.copy(id = MockData.ID, name = "profile").render()
        client.post()
                .uri("/custompages/profile")
                .syncBody(customPage)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyAs<CommonCustomPage>().isEqualWith(renderedPage)
    }

}
