package co.zsmb.site.backend.handlers

import co.zsmb.site.backend.Article
import co.zsmb.site.backend.ArticleRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import java.net.URI

@Component
class ArticleHandler(private val articleRepository: ArticleRepository) {

    fun getAllArticles(req: ServerRequest) = ok().body(articleRepository.findAll())

    fun getArticleById(req: ServerRequest): Mono<ServerResponse> {
        return articleRepository.findById(req.pathVariable("articleId"))
                .flatMap { ok().syncBody(it) }
                .switchIfEmpty(notFound().build())
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun createArticle(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<Article>()
                .flatMap(articleRepository::insert)
                .flatMap { article -> created(URI.create("/articles/${article.id}")).syncBody(article) }
    }

}
