package co.zsmb.site.backend

import org.springframework.context.support.beans
import org.springframework.web.reactive.function.server.RouterFunctionDsl
import org.springframework.web.reactive.function.server.ServerResponse.created
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import java.net.URI

fun beans() = beans {
    bean {
        router {
            addHelloRoutes()
            addArticleRoutes(ref())
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
    }
}
