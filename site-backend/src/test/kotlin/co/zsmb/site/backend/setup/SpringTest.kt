package co.zsmb.site.backend.setup

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import java.lang.annotation.Inherited

@SpringBootTest
@SpringJUnitConfig
@ActiveProfiles("test")
@ContextConfiguration(initializers = [TestInitializer::class])
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class SpringTest
