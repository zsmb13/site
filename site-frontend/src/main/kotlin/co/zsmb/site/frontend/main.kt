package co.zsmb.site.frontend

import co.zsmb.kagu.core.init.application
import co.zsmb.site.frontend.components.mock.MockComponent

fun main(args: Array<String>) = application {

    routing {
        defaultState {
            path = "/"
            handler = MockComponent
        }
    }

}
