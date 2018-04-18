package co.zsmb.site.frontend.components.article.summary

import co.zsmb.kagu.core.Component
import co.zsmb.kagu.core.Controller
import co.zsmb.kagu.core.di.inject
import co.zsmb.kagu.core.dom.onClick
import co.zsmb.kagu.core.lookup
import co.zsmb.kagu.services.attributes.Attributes
import co.zsmb.kagu.services.navigation.Navigator
import co.zsmb.site.frontend.util.appendMarkdown
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLHeadingElement
import org.w3c.dom.HTMLLinkElement

object ArticleSummaryComponent : Component(
        selector = "article-summary",
        templateUrl = "components/article/summary/summary.html",
        controller = ::ArticleSummaryController
)

class ArticleSummaryController : Controller() {

    val title by lookup<HTMLHeadingElement>("title")
    val titleLink by lookup<HTMLLinkElement>("titleLink")
    val summary by lookup<HTMLDivElement>("summary")

    val navigator by inject<Navigator>()

    val params by inject<Attributes>()

    override fun onCreate() {
        super.onCreate()

        title.textContent = params.getString("title")
        titleLink.onClick {
            navigator.goto("/${params.getStringUnsafe("url")}")
            it.preventDefault()
        }

        val summaryHtml = params.getStringUnsafe("summary")
        summary.appendMarkdown(summaryHtml)
    }

}
