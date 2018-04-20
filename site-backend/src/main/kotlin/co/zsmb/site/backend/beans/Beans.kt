package co.zsmb.site.backend.beans

import co.zsmb.site.backend.routingBeans
import co.zsmb.site.backend.security.securityBeans
import org.springframework.context.support.beans

fun commonBeans() = beans {
    routingBeans()
    securityBeans()
}

fun prodOnlyBeans() = beans {
    adminInitBeans()
}
