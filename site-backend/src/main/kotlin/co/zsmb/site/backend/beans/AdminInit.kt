package co.zsmb.site.backend.beans

import co.zsmb.site.backend.security.User
import co.zsmb.site.backend.security.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.support.BeanDefinitionDsl
import org.springframework.security.crypto.password.PasswordEncoder
import java.math.BigInteger
import java.util.*

fun BeanDefinitionDsl.adminInitBeans() {
    bean {
        CommandLineRunner {
            val userRepository = ref<UserRepository>()

            @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
            val userCount: Long = userRepository.count().block()
            if (userCount == 0L) {
                val pass = BigInteger(130, Random(System.currentTimeMillis())).toString(32)
                val encoder = ref<PasswordEncoder>()

                userRepository.insert(User(
                        name = "admin",
                        pass = encoder.encode(pass),
                        active = true,
                        roles = arrayOf("ROLE_ADMIN"))).block()

                println("Default admin user created with password: $pass")
            }
        }
    }
}
