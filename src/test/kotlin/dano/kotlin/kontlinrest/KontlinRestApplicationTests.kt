package dano.kotlin.kontlinrest

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext

@SpringBootTest
class KontlinRestApplicationTests {

    @Autowired
    private lateinit var context: ApplicationContext

    @Test
    fun `context loads`() {
        assertThat(context.beanDefinitionCount).isGreaterThan(0)
    }
}
