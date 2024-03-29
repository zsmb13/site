package co.zsmb.site.frontend.services.network

import co.zsmb.kagu.services.http.HttpService
import co.zsmb.site.common.ArticleDetail
import co.zsmb.site.common.ArticleSummary
import co.zsmb.site.common.CustomPage
import co.zsmb.site.frontend.config.Config
import kotlinx.serialization.json.JSON
import kotlinx.serialization.list
import kotlinx.serialization.serializer

class ApiServiceImpl(private val httpService: HttpService, private val config: Config) : ApiService {

    init {
        httpService.backoffStrategy = HttpService.BackoffStrategy.Exponential
    }

    private val baseUrl = config.BACKEND_ADDRESS

    override fun getArticleSummaries(callback: (List<ArticleSummary>) -> Unit) {
        httpService.get("$baseUrl/articlesummaries",
                onSuccess = {
                    val articleSummaries: List<ArticleSummary> = JSON.parse(ArticleSummary::class.serializer().list, it)
                    callback(articleSummaries)
                },
                onError = {
                    // TODO
                }
        )
    }

    override fun getArticleDetail(articleUrl: String, onSuccess: (ArticleDetail) -> Unit, onError: () -> Unit) {
        httpService.get("$baseUrl/articledetails/url/$articleUrl",
                onSuccess = {
                    val articleDetail = JSON.parse<ArticleDetail>(it)
                    onSuccess(articleDetail)
                },
                onError = { onError() })
    }

    override fun getCustomPage(name: String, onSuccess: (CustomPage) -> Unit) {
        httpService.get("$baseUrl/custompages/$name",
                onSuccess = {
                    val customPage = JSON.parse<CustomPage>(it)
                    onSuccess(customPage)
                },
                onError = {
                    // TODO
                })
    }

}
