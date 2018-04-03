package co.zsmb.site.backend.beans

import co.zsmb.site.backend.routingBeans
import co.zsmb.site.backend.security.securityBeans

fun beans() = org.springframework.context.support.beans {
    routingBeans()
    securityBeans()
}
