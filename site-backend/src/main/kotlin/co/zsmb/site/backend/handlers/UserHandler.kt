package co.zsmb.site.backend.handlers

import co.zsmb.site.backend.handlers.util.component1
import co.zsmb.site.backend.handlers.util.component2
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
import reactor.util.function.Tuples
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
                .flatMap { user ->
                    userRepository.findByName(user.name!!)
                            .map { repoUser -> Tuples.of(user, repoUser) }
                            .defaultIfEmpty(Tuples.of(user, User()))
                }
                .flatMap { (reqUser, repoUser) ->
                    if (repoUser.id == null) {
                        userRepository
                                .insert(reqUser.copy(pass = passwordEncoder.encode(reqUser.pass)))
                                .flatMap { user -> created(URI.create("/users/${user.id}")).syncBody(user) }
                    } else {
                        badRequest().build()
                    }
                }
                .withBoom()
    }

    fun updateUserById(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<User>()
                .flatMap { user ->
                    userRepository.findByName(user.name!!)
                            .map { repoUser -> Tuples.of(user, repoUser) }
                            .defaultIfEmpty(Tuples.of(user, User()))
                }
                .flatMap { (reqUser, repoUser) ->
                    val reqUserId = req.pathVariable("userId")
                    if (repoUser.id != reqUserId) {
                        badRequest().build()
                    } else {
                        val userToInsert = reqUser.copy(
                                id = reqUserId,
                                pass = passwordEncoder.encode(reqUser.pass))
                        userRepository
                                .save(userToInsert)
                                .flatMap { ok().syncBody(it) }
                    }
                }
                .withBoom()
    }

    fun removeUserById(req: ServerRequest): Mono<ServerResponse> {
        return userRepository.deleteById(req.pathVariable("userId"))
                .then(ok().build())
                .withBoom()
    }

}
