package co.zsmb.site.backend.handlers

import co.zsmb.site.backend.handlers.util.withBoom
import co.zsmb.site.backend.security.User
import co.zsmb.site.backend.security.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.net.URI

@Component
@PreAuthorize("hasRole('ADMIN')")
class UserHandler(private val userRepository: UserRepository,
                  private val passwordEncoder: PasswordEncoder) {

    fun getAllUsers(req: ServerRequest) = ok().body(userRepository.findAll())

    fun getUserById(req: ServerRequest): Mono<ServerResponse> {
        return userRepository.findById(req.pathVariable("userId"))
                .flatMap { ok().syncBody(it) }
                .switchIfEmpty(notFound().build())
    }

    fun createUser(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<User>()
                .flatMap { userRepository.insert(it.copy(pass = passwordEncoder.encode(it.pass))) }
                .flatMap { user -> created(URI.create("/users/${user.id}")).syncBody(user) }
                .withBoom()
    }

}

