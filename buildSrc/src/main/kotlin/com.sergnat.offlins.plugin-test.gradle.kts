import gradle.kotlin.dsl.accessors._493df1a2873b25a073d5f01feaf1ba6c.testing
import org.gradle.kotlin.dsl.base
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.`java-gradle-plugin`
import org.gradle.kotlin.dsl.`jvm-test-suite`
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

plugins {
    base
    `jvm-test-suite`
    `java-gradle-plugin`

    id("com.sergnat.offlins.functional-tests")
    id("com.sergnat.offlins.diff-coverage-conventions")
}

testing.suites {

    val test by getting(JvmTestSuite::class) {
        useJUnitJupiter()
    }
}

val functionalTestSuite: JvmTestSuite = testing.suites.getByName("functionalTest") as JvmTestSuite
configure<GradlePluginDevelopmentExtension> {
    testSourceSet(functionalTestSuite.sources)
}

tasks.check.configure {
    functionalTestSuite.targets.forEach { suiteTarget ->
        dependsOn(suiteTarget.testTask)
    }
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.jacocoTestReport {

    executionData.setFrom(fileTree(buildDir).include("/jacoco/*.exec"))
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
