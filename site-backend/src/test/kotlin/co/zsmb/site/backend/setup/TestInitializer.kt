package co.zsmb.site.backend.setup

import co.zsmb.site.backend.beans.commonBeans
import co.zsmb.site.backend.setup.mocks.testBeans
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

class TestInitializer : ApplicationContextInitializer<GenericApplicationContext> {

    override fun initialize(applicationContext: GenericApplicationContext) {
        commonBeans().initialize(applicationContext)
        testBeans().initialize(applicationContext)
    }

}
