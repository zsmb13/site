package co.zsmb.site.frontend.services.network

import co.zsmb.koinjs.dsl.module.applicationContext


val ApiModule = applicationContext {
    bean { ApiServiceImpl(get()) as ApiService }
}
