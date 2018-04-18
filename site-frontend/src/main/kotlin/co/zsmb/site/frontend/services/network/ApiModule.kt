package co.zsmb.site.frontend.services.network

import co.zsmb.koinjs.dsl.module.Module

object ApiModule : Module() {
    override fun context() = declareContext {
        provide { ApiServiceImpl(get()) as ApiService }
    }
}
