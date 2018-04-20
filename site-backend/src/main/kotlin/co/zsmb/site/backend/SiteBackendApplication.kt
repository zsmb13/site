package co.zsmb.site.backend

import co.zsmb.site.backend.beans.commonBeans
import co.zsmb.site.backend.beans.prodOnlyBeans
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class SiteBackendApplication

fun main(args: Array<String>) {
    runApplication<SiteBackendApplication>(*args) {
        addInitializers(commonBeans(), prodOnlyBeans())
    }
}
