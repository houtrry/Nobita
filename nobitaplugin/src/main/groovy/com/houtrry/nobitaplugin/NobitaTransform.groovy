package com.houtrry.nobitaplugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import com.houtrry.asm.ConsumingTimeClassAdapter
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes

class NobitaTransform extends Transform {

    NobitaTransform() {

    }

    @Override
    String getName() {
        return "nobita"
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
        println('===============================================================================================')
        println('=====================================nobita start==============================================')
        println('===============================================================================================')


        /**
         * transform要做的是copy指定内容到目标目录
         *      需要拷贝的内容分成两部分: jarInputs 和 directoryInputs.
         *      jarInputs:项目的各个依赖所生成的jar(dependencies中的各个依赖, 需要注意的是, dependencies中依赖的module也是生成jar后走这里)
         *      directoryInputs:当前project下的各个class文件
         *
         *      目标目录通过outputProvider.getContentLocation方法获取
         *
         *
         * 在拷贝directoryInputs下的各个class文件时, 可以介入asm来修改class文件内容.修改完继续执行copy过程, 新的class就会被打包到apk中.
         * 当然, 我们也可以考虑介入到jarInputs中jar包的拷贝过程
         *
         *
         */




        def inputs = transformInvocation.inputs
        def outputProvider = transformInvocation.outputProvider
        inputs.each {
            // jarInputs：各个依赖所编译成的 jar ⽂文件
            it.jarInputs.each { jarInput ->
                // dest:./app/build/intermediates/transforms/hencoderTransform/...
                File dest = outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                println "============>>>>>>jarInputs, from-> ${jarInput.file} to->${dest}"
                FileUtils.copyFile(jarInput.file, dest)
            }
            // directoryInputs：本地 project 编译成的多个 class ⽂文件存放的⽬目录
            it.directoryInputs.each { directoryInput ->
                // dest:./app/build/intermediates/transforms/hencoderTransform/...

                if (directoryInput.file.isDirectory()) {
                    directoryInput.file.eachFileRecurse { File file ->

                        def name = file.name
                        if (name.endsWith(".class") && !(name == ("R.class"))
                                && !name.startsWith("R\$") && !(name == ("BuildConfig.class"))) {

                            println "============>>>>>>directoryInput, file->${file}"

                            //asm相关
                            //asm使用 https://www.jianshu.com/p/905be2a9a700

                            //ClassReader这个类会将 .class 文件读入到 ClassReader 中的字节数组中，
                            // 它的 accept 方法接受一个 ClassVisitor 实现类，
                            // 并按照顺序调用 ClassVisitor 中的方法
                            ClassReader classReader = new ClassReader(file.bytes)
                            //ClassWriter 是一个 ClassVisitor 的子类，是和 ClassReader 对应的类，
                            // ClassReader 是将 .class 文件读入到一个字节数组中，
                            // ClassWriter 是将修改后的类的字节码内容以字节数组的形式输出。
                            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)


                            ConsumingTimeClassAdapter classAdapter = new ConsumingTimeClassAdapter(Opcodes.ASM7, classWriter)

                            classReader.accept(classAdapter, ClassReader.SKIP_FRAMES)
                            def byteResult = classWriter.toByteArray()
                            //直接写入到原文件
                            //内容覆盖
                            file.withOutputStream {
                                it.write(byteResult)
                            }
                        } else {

                        }

                    }
                } else {

                }

                File dest = outputProvider.getContentLocation(directoryInput.name, directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                println "============>>>>>>directoryInputs, from-> ${directoryInput.file} to->${dest}"

                FileUtils.copyDirectory(directoryInput.file, dest)
            }
            println('===============================================================================================')
            println('===================================== nobita end ==============================================')
            println('===============================================================================================')
        }
    }
}