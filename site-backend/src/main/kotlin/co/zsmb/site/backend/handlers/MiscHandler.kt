package co.zsmb.site.backend.handlers

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.ok

@Component
@PreAuthorize("hasRole('ADMIN')")
class MiscHandler {

    fun getHello(req: ServerRequest) = ok().build()

}
