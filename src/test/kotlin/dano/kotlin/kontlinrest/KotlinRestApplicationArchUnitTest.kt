package dano.kotlin.kontlinrest

import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AnalyzeClasses(packagesOf = [KotlinRestApplicationArchUnitTest::class])
internal class KotlinRestApplicationArchUnitTest {

    @ArchTest
    val `there are no package cycles` =
            slices().matching("$BASE_PACKAGE.(**)..")
                    .should().beFreeOfCycles()

    @ArchTest
    val `the domain model does not have outgoing dependencies` =
            noClasses().that().resideInAPackage("$DOMAIN_MODEL_PACKAGE..")
                    .should().accessClassesThat().resideInAnyPackage(
                            "$DOMAIN_SERVICE_PACKAGE..",
                            "$DOMAIN_DAO_PACKAGE..",
                            "$APPLICATION_PACKAGE..",
                            "$ADAPTER_PACKAGE..")

    @ArchTest
    val `the domain does not access the application and adapters` =
            noClasses().that().resideInAPackage("$DOMAIN_PACKAGE..")
                    .should().accessClassesThat().resideInAnyPackage("$APPLICATION_PACKAGE..", "$ADAPTER_PACKAGE..")

    @ArchTest
    val `the application does not access the adapters` =
            noClasses().that().resideInAPackage("$APPLICATION_PACKAGE..")
                    .should().accessClassesThat().resideInAPackage("$ADAPTER_PACKAGE..")

    @ArchTest
    val `one adapter should not access another adapter` =
            slices().matching("$ADAPTER_PACKAGE.(*)")
                    .should().notDependOnEachOther()

    companion object {
        private val BASE_PACKAGE = KotlinRestApplicationArchUnitTest::class.java.`package`.name

        private val DOMAIN_PACKAGE = "$BASE_PACKAGE.domain"
        private val DOMAIN_MODEL_PACKAGE = "$DOMAIN_PACKAGE.model"
        private val DOMAIN_SERVICE_PACKAGE = "$DOMAIN_PACKAGE.service"
        private val DOMAIN_DAO_PACKAGE = "$DOMAIN_PACKAGE.dao"

        private val APPLICATION_PACKAGE = "$BASE_PACKAGE.application"

        private val ADAPTER_PACKAGE = "$BASE_PACKAGE.adapter"
    }
}
