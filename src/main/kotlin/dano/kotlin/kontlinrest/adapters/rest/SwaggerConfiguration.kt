package dano.kotlin.kontlinrest.adapters.rest

import mu.KotlinLogging
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.info.GitProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.*
import springfox.documentation.swagger2.annotations.EnableSwagger2


private val log = KotlinLogging.logger {}

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration::class)
class SwaggerConfiguration(
        val buildInfo: BuildProperties,
        val gitInfo: GitProperties
) {
    @Bean
    fun api(): Docket {
        val version = "${buildInfo.version}-${gitInfo.shortCommitId}-${gitInfo.branch}"

        log.info { "Initialized API Docker for version: $version" }

        return Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(version))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths { it?.startsWith("/api/") ?: false }
                .build()
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
    }

    @Bean
    fun uiConfig() = UiConfiguration(
            java.lang.Boolean.TRUE,
            java.lang.Boolean.FALSE,
            1,
            1,
            ModelRendering.MODEL,
            java.lang.Boolean.FALSE,
            DocExpansion.LIST,
            java.lang.Boolean.FALSE,
            null,
            OperationsSorter.ALPHA,
            java.lang.Boolean.FALSE,
            TagsSorter.ALPHA,
            UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,
            null
    )

    private fun apiInfo(version: String) = ApiInfoBuilder()
            .title("API - Order Service")
            .description("Order Management")
            .version(version)
            .build()
}
