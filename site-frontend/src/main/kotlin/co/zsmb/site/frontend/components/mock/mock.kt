package co.zsmb.site.frontend.components.mock

import co.zsmb.kagu.core.Component
import co.zsmb.kagu.core.Controller
import co.zsmb.kagu.core.di.inject
import co.zsmb.kagu.services.logging.Logger

object MockComponent : Component(
        selector = "mock-component",
        templateUrl = "components/mock/mock.html",
        controller = ::MockController
)

class MockController : Controller() {

    private val logger by inject<Logger>()

    override fun onCreate() {
        super.onCreate()
        logger.i(this, "onCreate ran!")
    }

}
