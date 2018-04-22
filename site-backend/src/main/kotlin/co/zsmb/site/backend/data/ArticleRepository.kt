package co.zsmb.site.backend.data

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ArticleRepository : ReactiveMongoRepository<Article, String> {

    fun findByUrl(id: String): Mono<Article>

    fun findAllByOrderByPublishDateDesc(): Flux<Article>

}
