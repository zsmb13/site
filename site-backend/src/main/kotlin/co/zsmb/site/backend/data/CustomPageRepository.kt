package co.zsmb.site.backend.data

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface CustomPageRepository : ReactiveMongoRepository<CustomPage, String> {

    fun findByName(name: String): Mono<CustomPage>

    fun deleteByName(name: String): Mono<Int>

}
