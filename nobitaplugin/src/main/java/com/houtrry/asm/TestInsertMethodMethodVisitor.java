package com.houtrry.asm;


import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * @author: houtrry
 * @date: 2020/6/29 20:18
 * @version: $
 * @description: MethodVisitor 是一个抽象类，当 ASM 的 ClassReader 读取到 Method 时就转入 MethodVisitor 接口处理。
 * AdviceAdapter 是 MethodVisitor 的子类，使用 AdviceAdapter 可以更方便的修改方法的字节码
 */
public class TestInsertMethodMethodVisitor extends AdviceAdapter {

    private int startTimeId = -1;
    private boolean isAnnotationClass = false;
    private boolean isAnnotationMethod = false;

    private String className = "";
    private String methodName = "";

    /**
     * Creates a new {@link AdviceAdapter}.
     *
     * @param api    the ASM API version implemented by this visitor. Must be one
     *               of {@link Opcodes#ASM4} or {@link Opcodes#ASM5}.
     * @param mv     the method visitor to which this adapter delegates calls.
     * @param access the method's access flags (see {@link Opcodes}).
     * @param name   the method's name.
     * @param desc   the method's descriptor (see {@link Type Type}).
     */
    protected TestInsertMethodMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc, boolean isAnnotationClass, String className) {
        super(api, mv, access, name, desc);
        this.isAnnotationClass = isAnnotationClass;
        this.className = className;
        this.methodName = name;
    }

    /**
     * 表示 ASM 开始扫描这个方法
     */
    @Override
    public void visitCode() {
        super.visitCode();
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        System.out.println("===>>>>>>>>>>>>>>>>>>>>>>>>>>visitAnnotation, desc: "+desc);
        if (Constants.NOBITA_ANNOTATION.equals(desc)) {
            isAnnotationMethod = true;
        } else {
            System.out.println("===>>>>>>>>>>>>>>>>>>>>>>>>>>visitAnnotation, desc->1: "+desc);
            System.out.println("===>>>>>>>>>>>>>>>>>>>>>>>>>>visitAnnotation, desc->2: "+Constants.NOBITA_ANNOTATION);
        }
        System.out.println("===>>>>>>>>>>>>>>>>>>>>>>>>>>visitAnnotation, isAnnotationMethod: "+isAnnotationMethod);

        return super.visitAnnotation(desc, visible);
    }


    /**
     * 表示方法扫码完毕
     */
    @Override
    public void visitEnd() {
        super.visitEnd();
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }


    private int timeLocalIndex = 0;

    @Override
    protected void onMethodEnter() {
        System.out.println("===>>>>>>>>>>>>>>>>>>>>>>>>>>onMethodEnter, isAnnotationMethod: "+isAnnotationMethod+", isAnnotationClass: "+isAnnotationClass);

    }

    @Override
    protected void onMethodExit(int opcode) {

    }

    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
        System.out.println("===TestInsertMethodMethodVisitor, visitInsn, methodName: "+methodName+", opcode: "+opcode);
    }

    @Override
    public void visitVarInsn(int opcode, int var) {
        super.visitVarInsn(opcode, var);
        System.out.println("===TestInsertMethodMethodVisitor, visitVarInsn, methodName: "+methodName+", opcode: "+opcode+", var: "+var);
    }
}
