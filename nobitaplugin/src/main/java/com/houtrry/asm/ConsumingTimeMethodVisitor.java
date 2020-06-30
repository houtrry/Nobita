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
public class ConsumingTimeMethodVisitor extends AdviceAdapter {

    private int startTimeId = -1;
    private boolean isAnnotationClass = false;
    private boolean isAnnotationMethod = false;

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
    protected ConsumingTimeMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc, boolean isAnnotationClass) {
        super(api, mv, access, name, desc);
        this.isAnnotationClass = isAnnotationClass;
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
        if (Constants.NOBITA_ANNOTATION.equals(desc)) {
            isAnnotationMethod = true;
        }
        return super.visitAnnotation(desc, visible);
    }

    /**
     * 进入这个方法
     */
    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        startTimeId = newLocal(Type.LONG_TYPE);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitIntInsn(LSTORE, 1);
    }

    /**
     * 即将从这个方法出去
     * @param opcode
     */
    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        if (opcode == RETURN) {
            visitInsn(ACONST_NULL);
        } else if (opcode == ARETURN || opcode == ATHROW) {
            dup();
        } else {
            if (opcode == LRETURN || opcode == DRETURN) {
                dup2();
            } else {
                dup();
            }
            box(Type.getReturnType(this.methodDesc));
        }

        int costTimeId = newLocal(Type.LONG_TYPE);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitVarInsn(LLOAD, startTimeId);
        mv.visitInsn(LSUB);
        mv.visitVarInsn(LSTORE, costTimeId);
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitLdcInsn("costTime is ");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        mv.visitVarInsn(LLOAD, costTimeId);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
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

}
