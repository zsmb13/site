package co.zsmb.site.frontend.components.about

import co.zsmb.kagu.core.Component
import co.zsmb.kagu.core.Controller
import co.zsmb.kagu.core.di.inject
import co.zsmb.kagu.core.lookup
import co.zsmb.site.frontend.components.loading.addLoadingSpinner
import co.zsmb.site.frontend.services.network.ApiService
import co.zsmb.site.frontend.util.appendMarkdown
import co.zsmb.site.frontend.util.removeChildren
import org.w3c.dom.HTMLDivElement

object AboutComponent : Component(
        selector = "about-comp",
        templateUrl = "components/about/about.html",
        controller = ::AboutController
)

class AboutController : Controller() {

    val api by inject<ApiService>()

    val content by lookup<HTMLDivElement>("content")

    override fun onAdded() {
        super.onAdded()

        content.removeChildren()
        addLoadingSpinner(this, content)

        api.getCustomPage("about") { aboutPage ->
            content.removeChildren()
            content.appendMarkdown(aboutPage.content)
        }
    }

}
