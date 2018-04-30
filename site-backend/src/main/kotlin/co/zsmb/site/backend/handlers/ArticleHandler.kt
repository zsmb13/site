package co.zsmb.site.backend.handlers

import co.zsmb.site.backend.data.Article
import co.zsmb.site.backend.data.ArticleRepository
import co.zsmb.site.backend.handlers.util.component1
import co.zsmb.site.backend.handlers.util.component2
import co.zsmb.site.backend.handlers.util.withBoom
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.*
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Mono
import reactor.util.function.Tuples
import java.net.URI
import java.util.*

@Component
@PreAuthorize("hasRole('ADMIN')")
class ArticleHandler(private val articleRepository: ArticleRepository) {

    fun getAllArticles(req: ServerRequest) = ok().body(articleRepository.findAll())

    fun getArticleById(req: ServerRequest): Mono<ServerResponse> {
        return articleRepository.findById(req.pathVariable("articleId"))
                .flatMap { ok().syncBody(it) }
                .switchIfEmpty(notFound().build())
    }

    fun createArticle(req: ServerRequest): Mono<ServerResponse> {
        return req.bodyToMono<Article>()
                .map { it.copy(lastModificationDate = Date()) }
                .flatMap(articleRepository::insert)
                .flatMap { article -> created(URI.create("/articles/${article.id}")).syncBody(article) }
                .withBoom()
    }

    fun updateArticle(req: ServerRequest): Mono<ServerResponse> {
        val requestArticleId = req.pathVariable("articleId")
        return req.bodyToMono<Article>()
                .flatMap { reqArticle ->
                    articleRepository.findById(requestArticleId)
                            .transform { repoArticleMono ->
                                if (repoArticleMono == Mono.empty<Article>())
                                    throw IllegalArgumentException("No existing article to update")
                                else
                                    repoArticleMono
                            }
                            .map { repoArticle -> Tuples.of(reqArticle, repoArticle) }
                }
                .map { (reqArticle, repoArticle) ->
                    reqArticle
                            .setPermanentPropertiesFrom(repoArticle)
                            .copy(lastModificationDate = Date())
                }
                .flatMap { article ->
                    articleRepository
                            .save(article)
                            .flatMap { ok().syncBody(it) }
                }
                .withBoom()
    }

    private fun Article.setPermanentPropertiesFrom(otherArticle: Article): Article {
        return this.copy(
                id = otherArticle.id,
                url = otherArticle.url
        )
    }

    fun removeArticleById(req: ServerRequest): Mono<ServerResponse> {
        val articleId = req.pathVariable("articleId")
        return articleRepository.findById(articleId)
                .delayUntil { articleRepository.deleteById(articleId) }
                .flatMap { ok().syncBody(it) }
                .switchIfEmpty(notFound().build())
    }

}
