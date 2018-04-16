package co.zsmb.site.backend.handlers

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

@Component
class PublicArticleHandler(private val articleRepository: ArticleRepository) {

    fun getArticleDetailById(req: ServerRequest): Mono<ServerResponse> {
        return articleRepository.findById(req.pathVariable("articleId"))
                .map(Article::toDetail)
                .transform { ServerResponse.ok().body(it) }
                .switchIfEmpty(ServerResponse.notFound().build())
    }

    fun getAllArticleSummaries(req: ServerRequest): Mono<ServerResponse> {
        return articleRepository.findAll()
                .map(Article::toSummary)
                .transform { ServerResponse.ok().body(it) }
                .toMono()
    }

}
