package co.zsmb.site.backend.security

import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.http.HttpMethod
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

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
                .addArticlePaths()
                .addMiscPaths()
                .anyExchange().authenticated()
                .and().httpBasic()
                .and().build()
    }
}

private fun ServerHttpSecurity.AuthorizeExchangeSpec.addArticlePaths() = this
        .pathMatchers(HttpMethod.GET, "/articles/**").permitAll()
        .pathMatchers(HttpMethod.POST, "/articles/**").hasRole("ADMIN")

private fun ServerHttpSecurity.AuthorizeExchangeSpec.addMiscPaths() = this
        .pathMatchers(HttpMethod.GET, "/").hasRole("ADMIN")
