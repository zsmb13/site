package co.zsmb.site.backend

import co.zsmb.site.backend.security.User
import co.zsmb.site.backend.security.UserRepository
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.reactive.function.server.RouterFunctionDsl
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import java.net.URI

internal fun BeanDefinitionDsl.routingBeans() {
    bean {
        router {
            addHelloRoutes()
            addArticleRoutes(ref())
            addAuthRoutes(ref(), ref())
        }
    }
}

private fun RouterFunctionDsl.addHelloRoutes() {
    GET("/") { ok().build() }
}

private fun RouterFunctionDsl.addArticleRoutes(articleRepository: ArticleRepository) {
    "/articles".nest {
        POST("/") {
            it.bodyToMono<Article>()
                    .flatMap(articleRepository::insert)
                    .flatMap { article ->
                        created(URI.create("/articles/${article.id}")).syncBody(article)
                    }
        }
        GET("/") {
            ok().body(articleRepository.findAll())
        }
        GET("/{articleId}") {
            articleRepository.findById(it.pathVariable("articleId"))
                    .flatMap { ok().syncBody(it) }
                    .switchIfEmpty(notFound().build())
        }
    }
}

private fun RouterFunctionDsl.addAuthRoutes(userRepository: UserRepository, passwordEncoder: PasswordEncoder) {
    "/users".nest {
        POST("/") {
            it.bodyToMono<User>()
                    .flatMap {
                        userRepository.insert(it.copy(pass = passwordEncoder.encode(it.pass)))
                    }
                    .flatMap { user ->
                        created(URI.create("/users/${user.id}")).syncBody(user)
                    }
        }
        GET("/") {
            ok().body(userRepository.findAll())
        }
    }
}
