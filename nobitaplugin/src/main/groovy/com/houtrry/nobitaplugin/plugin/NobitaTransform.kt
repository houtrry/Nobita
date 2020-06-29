package com.houtrry.nobitaplugin.plugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils

/**
 * @author: houtrry
 * @date: 2020/6/29 17:44
 * @version: $
 * @description:
 */
class NobitaTransform : Transform() {
    override fun getName(): String {
        return "nobita"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        super.transform(transformInvocation)
        transformInvocation?.apply {
            inputs.forEach { transformInput ->
                // jarInputs：各个依赖所编译成的 jar ⽂文件
                transformInput.jarInputs.forEach {
                    val dest = outputProvider.getContentLocation(
                        it.name,
                        it.contentTypes,
                        it.scopes,
                        Format.JAR
                    )
                    println("============>>>>>>jarInputs, from-> ${it.file} to->${dest}")
                    FileUtils.copyFile(it.file, dest)
                }


                // directoryInputs：本地 project 编译成的多个 class ⽂文件存放的⽬目录
                transformInput.directoryInputs.forEach {
                    val dest = outputProvider.getContentLocation(
                        it.name,
                        it.contentTypes,
                        it.scopes,
                        Format.DIRECTORY
                    )
                    println("============>>>>>>directoryInputs, from-> ${it.file} to->${dest}")
                    FileUtils.copyDirectory(it.file, dest)
                }
            }
        }

    }
}