package co.zsmb.site.backend.setup

import co.zsmb.site.backend.setup.mocks.testBeans
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import co.zsmb.site.backend.beans.beans as prodBeans

class TestInitializer : ApplicationContextInitializer<GenericApplicationContext> {

    override fun initialize(applicationContext: GenericApplicationContext) {
        prodBeans().initialize(applicationContext)
        testBeans().initialize(applicationContext)
    }

}
