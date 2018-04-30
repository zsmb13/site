package co.zsmb.site.backend.handlers.pub

import co.zsmb.site.backend.data.Article
import co.zsmb.site.backend.data.ArticleRepository
import co.zsmb.site.backend.data.toDetail
import co.zsmb.site.backend.data.toSummary
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import java.util.*

@Component
class PublicArticleHandler(private val articleRepository: ArticleRepository) {

    fun getArticleDetailById(req: ServerRequest): Mono<ServerResponse> {
        val currentTimeMs = Date().time
        return articleRepository.findById(req.pathVariable("articleId"))
                .filter { it.publishDate.time <= currentTimeMs }
                .map { it.toDetail() }
                .flatMap { ServerResponse.ok().syncBody(it) }
                .switchIfEmpty(ServerResponse.notFound().build())
    }

    fun getArticleDetailByUrl(req: ServerRequest): Mono<ServerResponse> {
        val currentTimeMs = Date().time
        return articleRepository.findByUrl(req.pathVariable("url"))
                .filter { it.publishDate.time <= currentTimeMs }
                .map(Article::toDetail)
                .flatMap { ServerResponse.ok().syncBody(it) }
                .switchIfEmpty(ServerResponse.notFound().build())
    }

    fun getAllArticleSummaries(req: ServerRequest): Mono<ServerResponse> {
        val currentTimeMs = Date().time
        return articleRepository.findAllByOrderByPublishDateDesc()
                .filter { it.publishDate.time <= currentTimeMs }
                .map(Article::toSummary)
                .transform { ServerResponse.ok().body(it) }
                .toMono()
    }

}
