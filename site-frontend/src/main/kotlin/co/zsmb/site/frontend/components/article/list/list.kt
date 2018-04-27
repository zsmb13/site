package co.zsmb.site.frontend.components.article.list

import co.zsmb.kagu.core.Component
import co.zsmb.kagu.core.Controller
import co.zsmb.kagu.core.createComponent
import co.zsmb.kagu.core.di.inject
import co.zsmb.kagu.core.lookup
import co.zsmb.site.common.ArticleSummary
import co.zsmb.site.frontend.components.article.summary.ArticleSummaryComponent
import co.zsmb.site.frontend.services.network.ApiService
import co.zsmb.site.frontend.util.removeChildren
import org.w3c.dom.HTMLDivElement

object ArticleListComponent : Component(
        selector = "article-list",
        templateUrl = "components/article/list/list.html",
        controller = ::ArticleListController
)

class ArticleListController : Controller() {

    private val listRoot by lookup<HTMLDivElement>("list-root")

    private val apiService by inject<ApiService>()

    override fun onCreate() {
        super.onCreate()
        println("List created")
    }

    override fun onAdded() {
        super.onAdded()

        listRoot.removeChildren()
        co.zsmb.site.frontend.components.loading.addLoadingSpinner(this, listRoot)

        apiService.getArticleSummaries { summaries ->
            listRoot.removeChildren()
            summaries.forEach(::addArticleSummary)
        }
    }

    private fun addArticleSummary(articleSummary: ArticleSummary) {
        createComponent(listRoot, ArticleSummaryComponent,
                "title" to articleSummary.title,
                "summary" to articleSummary.summary,
                "url" to articleSummary.url)
    }

}
