package co.zsmb.site.frontend.components.article.list

import co.zsmb.kagu.core.Component
import co.zsmb.kagu.core.Controller
import co.zsmb.kagu.core.createComponent
import co.zsmb.kagu.core.di.inject
import co.zsmb.kagu.core.lookup
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

    val listRoot by lookup<HTMLDivElement>("list-root")

    val apiService by inject<ApiService>()

    override fun onCreate() {
        super.onCreate()
        println("List created")
    }

    override fun onAdded() {
        super.onAdded()

        apiService.getArticleSummaries { summaries ->
            listRoot.removeChildren()
            summaries.forEach {
                createComponent(listRoot, ArticleSummaryComponent,
                        "title" to it.title,
                        "summary" to it.summary,
                        "url" to it.url)
            }
        }
    }

}
