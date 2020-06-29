package com.houtrry.nobitaplugin.plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class NobitaPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("===============NobitaPlugin start=========================")

        def android = project.extensions.findByType(AppExtension.class)
        println "android is ${android.bootClasspath}"
        android.registerTransform(new NobitaTransform())

        println("===============NobitaPlugin end=========================")
    }
}