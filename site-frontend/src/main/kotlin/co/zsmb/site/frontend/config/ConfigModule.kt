package co.zsmb.site.frontend.config

import co.zsmb.koinjs.dsl.module.applicationContext


val ConfigModule = applicationContext {
    bean { ConfigImpl as Config }
}
