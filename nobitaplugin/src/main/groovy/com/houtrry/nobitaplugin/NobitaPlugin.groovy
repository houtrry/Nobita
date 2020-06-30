package com.houtrry.nobitaplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class NobitaPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println("===============NobitaPlugin start=========================")
        def baseExtension = project.extensions.findByType(BaseExtension.class)
        def android = project.extensions.findByType(AppExtension.class)
        println "android is ${android.bootClasspath}"
        println "baseExtension is ${baseExtension.bootClasspath}"
        baseExtension.registerTransform(new NobitaTransform())

        println("===============NobitaPlugin end=========================")
    }
}