package co.zsmb.site.backend.data

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface AnalyticsEventRepository : ReactiveMongoRepository<AnalyticsEvent, String>
