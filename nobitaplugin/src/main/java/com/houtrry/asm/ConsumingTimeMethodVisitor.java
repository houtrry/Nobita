package com.houtrry.asm;

import groovyjarjarasm.asm.MethodVisitor;

/**
 * @author: houtrry
 * @date: 2020/6/29 20:18
 * @version: $
 * @description:
 */
class ConsumingTimeMethodVisitor extends MethodVisitor {
    public ConsumingTimeMethodVisitor(int api) {
        super(api);
    }

    public ConsumingTimeMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }
}
