package com.sergnat.offlins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskContainer
import org.gradle.jvm.tasks.Jar
import java.io.File

class OfflinsPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            addConfigurationWithDependency(JACOCO_CONFIGURATION, JACOCO_ANT)
            addConfigurationWithDependency(JACOCO_RUNTIME_CONFIGURATION, JACOCO_AGENT)

            with(tasks) {
                val instrumentClassesTask = create(INSTRUMENT_CLASSES_TASK, InstrumentClassesOfflineTask::class.java)
                createAssembleInstrumentedJarTask(instrumentClassesTask.instrumentedClassesDir)

                create(OFFLINS_TASK).doLast {
                    println("Currently, I do nothing")
                }
            }
        }
    }

    private fun TaskContainer.createAssembleInstrumentedJarTask(instrumentedClassesDir: File) {
        create(ASSEMBLE_INSTRUMENTED_JAR_TASK, Jar::class.java).apply {
            description = "Assemble Jar with instrumented classes"
            dependsOn += INSTRUMENT_CLASSES_TASK

            from(instrumentedClassesDir)
            archiveBaseName.set("${project.name}-$INSTRUMENTED_JAR_SUFFIX")
        }
    }

    private fun Project.addConfigurationWithDependency(
        configurationName: String,
        jacocoDependency: JacocoDependency
    ) {
        val jacocoRuntimeConfiguration: Configuration = configurations.create(configurationName)
        dependencies.add(
            jacocoRuntimeConfiguration.name,
            jacocoDependency.buildDependency(dependencies)
        )
    }

    companion object {
        const val OFFLINS_TASK = "jacocoReport"
        const val INSTRUMENT_CLASSES_TASK = "instrumentClassesOffline"

        const val ASSEMBLE_INSTRUMENTED_JAR_TASK = "assembleInstrumentedJar"
        const val INSTRUMENTED_JAR_SUFFIX = "instrumented"

        const val JACOCO_CONFIGURATION = "jacoco"
        const val JACOCO_RUNTIME_CONFIGURATION = "jacocoRuntime"
    }

}
