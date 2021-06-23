package com.todoclean;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;


import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.todoclean",
        importOptions = { ImportOption.DoNotIncludeTests.class })
class ArchitectureTest {

    private static final String DOMAIN_LAYER = "com.todoclean.domain..";

    private static final String APPLICATION_LAYER = "com.todoclean.application..";

    private static final String ADAPTERS_LAYER = "com.todoclean.adapters..";

    private static final String JDK = "java..";

    private static final String SPRING = "org.springframework..";

    @ArchTest
    static final ArchRule domain_layer_rule =
            classes().that().resideInAPackage(DOMAIN_LAYER)
                    .should().onlyAccessClassesThat().resideInAnyPackage(DOMAIN_LAYER, JDK)
                    .because("Domain should only access itself and jdk");

    @ArchTest
    static final ArchRule application_layer_rule =
            classes().that().resideInAPackage(APPLICATION_LAYER)
                    .should().onlyAccessClassesThat().resideInAnyPackage(DOMAIN_LAYER, APPLICATION_LAYER, JDK)
                    .because("Application should only access itself, domain and jdk");

    @ArchTest
    static final ArchRule adapters_layer_rule =
            classes().that().resideInAPackage(ADAPTERS_LAYER)
                    .should().onlyAccessClassesThat().resideInAnyPackage(DOMAIN_LAYER, APPLICATION_LAYER, ADAPTERS_LAYER, JDK, SPRING)
                    .because("Adapters should only access itself, application, domain, spring and jdk");

}
