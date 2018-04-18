package co.zsmb.site.backend.handlers.pub

import co.zsmb.site.backend.data.CustomPage
import co.zsmb.site.backend.data.CustomPageRepository
import co.zsmb.site.backend.data.render
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Component
class PublicCustomPageHandler(private val customPageRepository: CustomPageRepository) {

    fun getPageByName(req: ServerRequest): Mono<ServerResponse> {
        return customPageRepository.findByName(req.pathVariable("name"))
                .map(CustomPage::render)
                .flatMap { ServerResponse.ok().syncBody(it) }
                .switchIfEmpty(ServerResponse.notFound().build())
    }

}
