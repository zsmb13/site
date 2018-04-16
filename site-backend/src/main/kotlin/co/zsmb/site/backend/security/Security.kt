package co.zsmb.site.backend.security

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.config.WebFluxConfigurerComposite

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration

internal fun BeanDefinitionDsl.securityBeans() {
    bean<PasswordEncoder> {
        BCryptPasswordEncoder()
    }
    bean<ReactiveUserDetailsService> {
        ReactiveUserDetailsService {
            ref<UserRepository>().findByName(it).cast(UserDetails::class.java)
        }
    }
    bean<SecurityWebFilterChain> {
        ref<ServerHttpSecurity>()
                .csrf().disable()
                .authorizeExchange()
                .anyExchange().permitAll()
                .and().httpBasic()
                .and().build()
    }
    bean<WebFluxConfigurer> {
        object : WebFluxConfigurerComposite() {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/public/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET")
            }
        }
    }
}
