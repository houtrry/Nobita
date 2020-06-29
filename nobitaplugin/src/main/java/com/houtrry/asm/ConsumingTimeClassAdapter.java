package com.houtrry.asm;


import org.objectweb.asm.Opcodes;

import groovyjarjarasm.asm.ClassVisitor;
import groovyjarjarasm.asm.MethodVisitor;

/**
 * @author: houtrry
 * @date: 2020/6/29 20:08
 * @version: $
 * @description:
 */
public class ConsumingTimeClassAdapter extends ClassVisitor {
    public ConsumingTimeClassAdapter(int api) {
        super(api);
    }

    public ConsumingTimeClassAdapter(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
        methodVisitor = new ConsumingTimeMethodVisitor(Opcodes.ASM5, methodVisitor);
        return methodVisitor;
    }
}
