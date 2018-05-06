package co.zsmb.site.backend.handlers

import co.zsmb.site.backend.data.CustomPage
import co.zsmb.site.backend.data.CustomPageRepository
import co.zsmb.site.backend.data.render
import co.zsmb.site.backend.handlers.util.withBoom
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.net.URI

@Component
@PreAuthorize("hasRole('ADMIN')")
class CustomPageHandler(private val customPageRepository: CustomPageRepository) {

    fun createOrUpdateCustomPage(req: ServerRequest): Mono<ServerResponse> {
        val pageName = req.pathVariable("name")

        return customPageRepository.deleteByName(pageName)
                .then(req.bodyToMono<CustomPage>())
                .map { it.copy(name = pageName) }
                .flatMap(customPageRepository::insert)
                .flatMap { customPage ->
                    ServerResponse
                            .created(URI.create("/custompages/${customPage.name}"))
                            .syncBody(customPage.render())
                }
                .withBoom()
    }

    fun getAllCustomPages(req: ServerRequest) = ServerResponse.ok().body(customPageRepository.findAll())

}
