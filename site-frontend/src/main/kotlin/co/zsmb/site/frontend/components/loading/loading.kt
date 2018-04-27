package co.zsmb.site.frontend.components.loading

import co.zsmb.kagu.core.Component
import co.zsmb.kagu.core.Controller
import co.zsmb.kagu.core.createComponent
import org.w3c.dom.HTMLElement

object LoadingComponent : Component(
        selector = "loading-spinner",
        templateUrl = "components/loading/loading.html",
        controller = ::LoadingController
)

class LoadingController : Controller()

fun addLoadingSpinner(controller: Controller, root: HTMLElement) {
    controller.createComponent(root, LoadingComponent)
}
