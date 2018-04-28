package co.zsmb.site.frontend.components.article.detail

import co.zsmb.kagu.core.Component
import co.zsmb.kagu.core.Controller
import co.zsmb.kagu.core.di.inject
import co.zsmb.kagu.core.lookup
import co.zsmb.kagu.services.navigation.Navigator
import co.zsmb.kagu.services.pathparams.PathParams
import co.zsmb.site.common.ArticleDetail
import co.zsmb.site.frontend.components.loading.addLoadingSpinner
import co.zsmb.site.frontend.services.network.ApiService
import co.zsmb.site.frontend.util.CodeUtil
import co.zsmb.site.frontend.util.appendMarkdown
import co.zsmb.site.frontend.util.removeChildren
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLHeadingElement

object ArticleDetailComponent : Component(
        selector = "mock-component",
        templateUrl = "components/article/detail/detail.html",
        controller = ::ArticleDetailController
)

class ArticleDetailController : Controller() {

    private val pathParams by inject<PathParams>()
    private val apiService by inject<ApiService>()
    private val navigator by inject<Navigator>()

    private val title by lookup<HTMLHeadingElement>("title")
    private val contentRoot by lookup<HTMLElement>("content-root")

    override fun onAdded() {
        val articleUrl = pathParams.getStringUnsafe("articleUrl")

        title.textContent = ""
        contentRoot.removeChildren()
        addLoadingSpinner(this, contentRoot)

        apiService.getArticleDetail(articleUrl, ::displayArticle, ::onError)
    }

    private fun displayArticle(articleDetail: ArticleDetail) {
        title.textContent = articleDetail.title

        contentRoot.removeChildren()
        contentRoot.appendMarkdown(articleDetail.content)
        CodeUtil.highlightKotlinCodeBlocks(contentRoot)
    }

    private fun onError() {
        navigator.goto("/404")
    }

}
