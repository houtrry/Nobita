package com.houtrry.nobitaplugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.houtrry.asm.ConsumingTimeClassAdapter
import groovyjarjarasm.asm.ClassReader
import groovyjarjarasm.asm.ClassWriter

class NobitaTransform extends Transform {

    NobitaTransform() {

    }

    @Override
    String getName() {
        return "=BytecodeFixTransform="
    }

    /**
     * 该方法表示指定输入类型，这里我们指定CONTENT_RESOURCES类型
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    /**
     * 该方法表示当前Transform的作用范围，这里我们指定SCOPE_FULL_PROJECT
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    /**
     * 该方法表示当前Transform是否支持增量编译
     * @return
     */
    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws
            TransformException, InterruptedException, IOException {
        def inputs = transformInvocation.inputs
        def outputProvider = transformInvocation.outputProvider
        inputs.each {
            // jarInputs：各个依赖所编译成的 jar ⽂文件
            it.jarInputs.each { jarInput->
                // dest:./app/build/intermediates/transforms/hencoderTransform/...
                File dest = outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                println "============>>>>>>jarInputs, from-> ${jarInput.file} to->${dest}"
                FileUtils.copyFile(jarInput.file, dest)
            }
            // directoryInputs：本地 project 编译成的多个 class ⽂文件存放的⽬目录
            it.directoryInputs.each {directoryInput->
                // dest:./app/build/intermediates/transforms/hencoderTransform/...


                ClassReader classReader = new ClassReader(directoryInput.file.bytes)
                ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                ConsumingTimeClassAdapter classAdapter = new ConsumingTimeClassAdapter(classWriter)


                classReader.accept(classAdapter, ClassReader.SKIP_FRAMES)
                def byteResult = classWriter.toByteArray()





                File dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                println "============>>>>>>directoryInputs, from-> ${directoryInput.file} to->${dest}"

//                FileUtils.copyDirectory(directoryInput.file, dest)

                dest.withOutputStream {
                    it.write(byteResult)
                }
            }
        }
    }
}